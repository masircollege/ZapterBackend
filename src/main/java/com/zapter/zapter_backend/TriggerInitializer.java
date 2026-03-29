package com.zapter.zapter_backend;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TriggerInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    // Runs AFTER the application is fully started — meaning Hibernate has already
    // created/updated all tables, and DummyDataGenerator has already seeded data.
    // This guarantees the inventory table exists before we create the trigger on it.
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTriggers() {

        // ── Step 1: Create the trigger function ───────────────────────────────
        entityManager.createNativeQuery("""
                CREATE OR REPLACE FUNCTION update_product_stock_status()
                RETURNS TRIGGER AS $$
                DECLARE
                    v_total_quantity  INTEGER;
                    v_min_count       INTEGER;
                    v_new_status      VARCHAR(20);
                BEGIN
                    SELECT
                        COALESCE(SUM(quantity), 0),
                        COALESCE(MIN(min_count), 0)
                    INTO
                        v_total_quantity,
                        v_min_count
                    FROM inventory
                    WHERE product_id = NEW.product_id;

                    IF v_total_quantity = 0 THEN
                        v_new_status := 'OUT_OF_STOCK';
                    ELSIF v_total_quantity <= (v_min_count * 1.75) THEN
                        v_new_status := 'LOW_IN_STOCK';
                    ELSE
                        v_new_status := 'AVAILABLE';
                    END IF;

                    UPDATE products
                    SET stock_status = v_new_status
                    WHERE id = NEW.product_id;

                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql
                """).executeUpdate();

        // ── Step 2: Drop old trigger if it exists, then recreate ──────────────
        // DROP IF EXISTS ensures this is safe to run on every startup
        // (which happens with create-drop ddl-auto since tables are recreated).
        entityManager.createNativeQuery(
                "DROP TRIGGER IF EXISTS trg_update_product_stock_status ON inventory"
        ).executeUpdate();

        entityManager.createNativeQuery("""
                CREATE TRIGGER trg_update_product_stock_status
                AFTER INSERT OR UPDATE OF quantity, min_count
                ON inventory
                FOR EACH ROW
                EXECUTE FUNCTION update_product_stock_status()
                """).executeUpdate();

        // ── Step 3: Backfill stock_status for rows already seeded ─────────────
        // DummyDataGenerator runs before this and inserts inventory rows,
        // but the trigger didn't exist yet at that point so stock_status
        // was never updated from the default AVAILABLE.
        // This recalculates stock_status for every product using the seeded data.
        entityManager.createNativeQuery("""
                UPDATE products p
                SET stock_status = (
                    SELECT
                        CASE
                            WHEN COALESCE(SUM(i.quantity), 0) = 0
                                THEN 'OUT_OF_STOCK'
                            WHEN COALESCE(SUM(i.quantity), 0) <= (COALESCE(MIN(i.min_count), 0) * 1.75)
                                THEN 'LOW_IN_STOCK'
                            ELSE
                                'AVAILABLE'
                        END
                    FROM inventory i
                    WHERE i.product_id = p.id
                )
                WHERE EXISTS (
                    SELECT 1 FROM inventory i WHERE i.product_id = p.id
                )
                """).executeUpdate();

        System.out.println("✅ TriggerInitializer: stock_status trigger created and backfill complete.");
    }
}