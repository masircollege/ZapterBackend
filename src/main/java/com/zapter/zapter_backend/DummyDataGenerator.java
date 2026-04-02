package com.zapter.zapter_backend;

import com.zapter.zapter_backend.measurement.domain.MeasurementType;
import com.zapter.zapter_backend.measurement.domain.ProductMeasurement;
import com.zapter.zapter_backend.measurement.domain.Unit;
import com.zapter.zapter_backend.measurement.enums.MeasurementCategory;
import com.zapter.zapter_backend.measurement.repository.MeasurementTypeRepository;
import com.zapter.zapter_backend.measurement.repository.ProductMeasurementRepository;
import com.zapter.zapter_backend.measurement.repository.UnitRepository;
import com.zapter.zapter_backend.product.domain.*;
import com.zapter.zapter_backend.product.enums.StockStatus;
import com.zapter.zapter_backend.product.repository.*;
import com.zapter.zapter_backend.user.domain.Vendor;
import com.zapter.zapter_backend.user.repository.UserRepository;
import com.zapter.zapter_backend.user.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DummyDataGenerator implements CommandLineRunner {

    // ── Repositories ──────────────────────────────────────────────────────────

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final KeyFeaturesRepository keyFeaturesRepository;
    private final FeatureCategoryRepository featureCategoryRepository;
    private final ProductKeyFeatureRepository productKeyFeatureRepository;
    private final MeasurementTypeRepository measurementTypeRepository;
    private final UnitRepository unitRepository;
    private final ProductMeasurementRepository productMeasurementRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final VendorRepository vendorRepository;
    private final InventoryRepository inventoryRepository;

    public DummyDataGenerator(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            ModelRepository modelRepository,
            KeyFeaturesRepository keyFeaturesRepository,
            FeatureCategoryRepository featureCategoryRepository,
            ProductKeyFeatureRepository productKeyFeatureRepository,
            MeasurementTypeRepository measurementTypeRepository,
            UnitRepository unitRepository,
            ProductDetailsRepository productDetailsRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            VendorRepository vendorRepository,
            InventoryRepository inventoryRepository,
            ProductMeasurementRepository productMeasurementRepository, UserRepository userRepository) {

        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.keyFeaturesRepository = keyFeaturesRepository;
        this.featureCategoryRepository = featureCategoryRepository;
        this.productKeyFeatureRepository = productKeyFeatureRepository;
        this.measurementTypeRepository = measurementTypeRepository;
        this.unitRepository = unitRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.vendorRepository = vendorRepository;
        this.inventoryRepository = inventoryRepository;
        this.productMeasurementRepository = productMeasurementRepository;
    }

    // ── Helper methods ────────────────────────────────────────────────────────

    private Category createCategory(String name) {
        Category c = new Category();
        c.setName(name);
        return categoryRepository.save(c);
    }

    private Brand createBrand(String name) {
        Brand b = new Brand();
        b.setName(name);
        return brandRepository.save(b);
    }

    private Model createModel(String name, Brand brand) {
        Model m = new Model();
        m.setName(name);
        m.setBrandModel(brand);
        return modelRepository.save(m);
    }

    private KeyFeatures createKeyFeature(String featureName) {
        KeyFeatures kf = new KeyFeatures();
        kf.setFeatureName(featureName);
        return keyFeaturesRepository.save(kf);
    }

    private void createFeatureCategory(KeyFeatures kf, Category category) {
        FeatureCategory fc = new FeatureCategory();
        fc.setKeyFeatures(kf);
        fc.setCategory(category);
        featureCategoryRepository.save(fc);
    }

    private MeasurementType createMeasurementType(String name, MeasurementCategory category, String description) {
        MeasurementType mt = new MeasurementType();
        mt.setName(name);
        mt.setMeasurementCategory(category);
        mt.setDescription(description);
        return measurementTypeRepository.save(mt);
    }

    private Unit createUnit(String name, String symbol, MeasurementCategory category,
                            BigDecimal conversionFactor, boolean isBase) {
        Unit u = new Unit();
        u.setName(name);
        u.setSymbol(symbol);
        u.setMeasurementCategory(category);
        u.setConversionFactor(conversionFactor);
        u.setIsBaseUnit(isBase);
        return unitRepository.save(u);
    }

    private Product createProduct(String name, String color, String description,
                                  BigDecimal price, Category category, Brand brand, Model model) {
        Product p = new Product();
        p.setName(name);
        p.setColor(color);
        p.setDescription(description);
        p.setPrice(price);
        p.setCategory(category);
        p.setBrand(brand);
        p.setModel(model);
        p.setStockStatus(StockStatus.AVAILABLE);
        return productRepository.save(p);
    }

    private void addProductMeasurement(Product product, MeasurementType type, Unit unit, BigDecimal value) {
        ProductMeasurement pm = new ProductMeasurement();
        pm.setMeasurementProduct(product);
        pm.setMeasurementType(type);
        pm.setUnit(unit);
        pm.setValue(value);
        productMeasurementRepository.save(pm);
    }

    private void addProductKeyFeature(Product product, KeyFeatures kf, String value) {
        ProductKeyFeature pkf = new ProductKeyFeature();
        pkf.setProdFeature(product);
        pkf.setKeyFeatures(kf);
        pkf.setValue(value);
        productKeyFeatureRepository.save(pkf);
    }

    private void addProductDetail(Product product, String specName, String specDetail) {
        ProductDetails pd = new ProductDetails();
        pd.setDetailsProduct(product);
        pd.setSpecificationName(specName);
        pd.setSpecificationDetails(specDetail);
        productDetailsRepository.save(pd);
    }

    private Warehouse createWarehouse(String name, String address) {
        Warehouse w = new Warehouse();
        w.setName(name);
        w.setAddress(address);
        return warehouseRepository.save(w);
    }

    private Vendor createVendor(String name, String address) {
        Vendor v = new Vendor();
        v.setName(name);
        v.setAddress(address);
        return vendorRepository.save(v);
    }

    private void createInventory(Product product, Warehouse warehouse, Vendor vendor,
                                 int quantity, int minCount) {
        Inventory inv = new Inventory();
        inv.setInventoryProduct(product);
        inv.setInventoryWarehouse(warehouse);
        inv.setInventoryVendor(vendor);
        inv.setQuantity(quantity);
        inv.setMinimumCount(minCount);
        inventoryRepository.save(inv);
    }

    // ── Main seed method ──────────────────────────────────────────────────────

    @Override
    public void run(String... args) throws Exception {

        if (productRepository.count() > 0) {
            System.out.println("⏭️ Data already seeded, skipping...");
            return;
        }

        // ── 1. CATEGORIES ─────────────────────────────────────────────────────
        Category laptops      = createCategory("Laptop");
        Category headphones   = createCategory("Headphones");
        Category monitors     = createCategory("Monitor");
        Category smartphones  = createCategory("Smartphone");
        Category tablets      = createCategory("Tablet");
        Category accessories  = createCategory("Computer Accessories");

        // ── 2. BRANDS ─────────────────────────────────────────────────────────
        Brand samsung  = createBrand("Samsung");
        Brand apple    = createBrand("Apple");
        Brand sony     = createBrand("Sony");
        Brand dell     = createBrand("Dell");
        Brand lg       = createBrand("LG");
        Brand logitech = createBrand("Logitech");
        Brand oneplus  = createBrand("OnePlus");

        // ── 3. MODELS ─────────────────────────────────────────────────────────
        // Samsung
        Model galaxyBook3Pro    = createModel("Galaxy Book3 Pro 360", samsung);
        Model galaxyS24Ultra    = createModel("Galaxy S24 Ultra", samsung);
        Model galaxyTab9        = createModel("Galaxy Tab S9", samsung);
        Model odysseyG7         = createModel("Odyssey G7", samsung);

        // Apple
        Model macbookProM3      = createModel("MacBook Pro M3", apple);
        Model iphone15Pro       = createModel("iPhone 15 Pro", apple);
        Model ipadPro           = createModel("iPad Pro M2", apple);
        Model airpodsProModel   = createModel("AirPods Pro 2nd Gen", apple);

        // Sony
        Model wh1000xm5         = createModel("WH-1000XM5", sony);
        Model wf1000xm5         = createModel("WF-1000XM5", sony);

        // Dell
        Model xps15             = createModel("XPS 15 9530", dell);
        Model u2723d            = createModel("UltraSharp U2723D", dell);

        // LG
        Model lgUltra27         = createModel("UltraFine 27UN850", lg);

        // Logitech
        Model mxMaster3s        = createModel("MX Master 3S", logitech);
        Model mxKeys            = createModel("MX Keys", logitech);

        // OnePlus
        Model op12              = createModel("OnePlus 12", oneplus);

        // ── 4. KEY FEATURES ───────────────────────────────────────────────────
        KeyFeatures processor       = createKeyFeature("Processor");
        KeyFeatures ram             = createKeyFeature("RAM");
        KeyFeatures storage         = createKeyFeature("Storage");
        KeyFeatures battery         = createKeyFeature("Battery");
        KeyFeatures noiseCancelling = createKeyFeature("Noise Cancelling");
        KeyFeatures refreshRate     = createKeyFeature("Refresh Rate");
        KeyFeatures camera          = createKeyFeature("Camera");
        KeyFeatures connectivity    = createKeyFeature("Connectivity");
        KeyFeatures os              = createKeyFeature("Operating System");

        // ── 5. FEATURE-CATEGORY LINKS ─────────────────────────────────────────
        createFeatureCategory(processor,       laptops);
        createFeatureCategory(ram,             laptops);
        createFeatureCategory(storage,         laptops);
        createFeatureCategory(battery,         laptops);
        createFeatureCategory(refreshRate,     monitors);
        createFeatureCategory(noiseCancelling, headphones);
        createFeatureCategory(battery,         headphones);
        createFeatureCategory(camera,          smartphones);
        createFeatureCategory(processor,       smartphones);
        createFeatureCategory(os,              smartphones);
        createFeatureCategory(processor,       tablets);
        createFeatureCategory(storage,         tablets);
        createFeatureCategory(connectivity,    accessories);

        // ── 6. MEASUREMENT TYPES & UNITS ─────────────────────────────────────
        MeasurementType screenSize  = createMeasurementType("Screen Size",   MeasurementCategory.LENGTH,         "Diagonal screen size");
        MeasurementType weight      = createMeasurementType("Weight",        MeasurementCategory.WEIGHT,         "Device weight");
        MeasurementType resolution  = createMeasurementType("Resolution",    MeasurementCategory.DIGITAL_DISPLAY,"Display resolution in megapixels");
        MeasurementType batteryLife = createMeasurementType("Battery Life",  MeasurementCategory.TIME,           "Hours of battery on single charge");

        Unit inch    = createUnit("Inch",  "in",  MeasurementCategory.LENGTH,         BigDecimal.valueOf(0.0254),  false);
        Unit gram    = createUnit("Gram",  "g",   MeasurementCategory.WEIGHT,         BigDecimal.valueOf(0.001),   false);
        Unit megapix = createUnit("Megapixel", "MP", MeasurementCategory.DIGITAL_DISPLAY, BigDecimal.ONE,         true);
        Unit hour    = createUnit("Hour",  "hr",  MeasurementCategory.TIME,           BigDecimal.valueOf(3600),    false);

        // ── 7. WAREHOUSES ─────────────────────────────────────────────────────
        Warehouse warehouseMumbai   = createWarehouse("Mumbai Central Warehouse",   "BKC, Bandra East, Mumbai - 400051");
        Warehouse warehouseDelhi    = createWarehouse("Delhi NCR Warehouse",         "Sector 63, Noida, UP - 201301");

        // ── 8. VENDORS ────────────────────────────────────────────────────────
        Vendor techDistributors  = createVendor("Tech Distributors Pvt Ltd",  "Lamington Road, Mumbai - 400007");
        Vendor apexElectronics   = createVendor("Apex Electronics",           "Nehru Place, New Delhi - 110019");

        // ── 9. PRODUCTS (18 products) ─────────────────────────────────────────

        // ── LAPTOPS ──────────────────────────────────────────────────────────

        // 1. Samsung Galaxy Book3 Pro 360
        Product book3Pro = createProduct(
                "Samsung Galaxy Book3 Pro 360",
                "Beige", "Premium 2-in-1 laptop with AMOLED display and S Pen",
                BigDecimal.valueOf(189999), laptops, samsung, galaxyBook3Pro);
        addProductMeasurement(book3Pro, screenSize, inch, BigDecimal.valueOf(16.0));
        addProductMeasurement(book3Pro, weight,     gram, BigDecimal.valueOf(1560));
        addProductKeyFeature(book3Pro, processor, "Intel Core i7-1360P");
        addProductKeyFeature(book3Pro, ram,       "16GB LPDDR5");
        addProductKeyFeature(book3Pro, storage,   "512GB NVMe SSD");
        addProductKeyFeature(book3Pro, battery,   "76Wh — up to 18 hours");
        addProductDetail(book3Pro, "Display",      "16-inch 3K AMOLED, 120Hz");
        addProductDetail(book3Pro, "Graphics",     "Intel Iris Xe Graphics");
        addProductDetail(book3Pro, "OS",           "Windows 11 Home");
        addProductDetail(book3Pro, "Ports",        "2x Thunderbolt 4, 1x USB-A, HDMI 2.0, MicroSD");
        createInventory(book3Pro, warehouseMumbai, techDistributors, 45, 10);

        // 2. Apple MacBook Pro M3
        Product macbookM3 = createProduct(
                "Apple MacBook Pro 14-inch M3",
                "Space Black", "Professional laptop powered by Apple M3 chip with Liquid Retina XDR display",
                BigDecimal.valueOf(199900), laptops, apple, macbookProM3);
        addProductMeasurement(macbookM3, screenSize, inch, BigDecimal.valueOf(14.2));
        addProductMeasurement(macbookM3, weight,     gram, BigDecimal.valueOf(1550));
        addProductKeyFeature(macbookM3, processor, "Apple M3 — 8-core CPU, 10-core GPU");
        addProductKeyFeature(macbookM3, ram,       "18GB Unified Memory");
        addProductKeyFeature(macbookM3, storage,   "512GB SSD");
        addProductKeyFeature(macbookM3, battery,   "72.4Wh — up to 22 hours");
        addProductDetail(macbookM3, "Display",     "14.2-inch Liquid Retina XDR, ProMotion 120Hz");
        addProductDetail(macbookM3, "OS",          "macOS Sonoma");
        addProductDetail(macbookM3, "Ports",       "3x Thunderbolt 4, HDMI, SD Card, MagSafe 3");
        addProductDetail(macbookM3, "Camera",      "1080p FaceTime HD");
        createInventory(macbookM3, warehouseMumbai, apexElectronics, 30, 8);

        // 3. Dell XPS 15
        Product xps15Product = createProduct(
                "Dell XPS 15 9530",
                "Platinum Silver", "High-performance laptop with OLED display for creators and professionals",
                BigDecimal.valueOf(159999), laptops, dell, xps15);
        addProductMeasurement(xps15Product, screenSize, inch, BigDecimal.valueOf(15.6));
        addProductMeasurement(xps15Product, weight,     gram, BigDecimal.valueOf(1855));
        addProductKeyFeature(xps15Product, processor, "Intel Core i9-13900H");
        addProductKeyFeature(xps15Product, ram,       "32GB DDR5");
        addProductKeyFeature(xps15Product, storage,   "1TB PCIe NVMe SSD");
        addProductKeyFeature(xps15Product, battery,   "86Wh — up to 13 hours");
        addProductDetail(xps15Product, "Display",     "15.6-inch 3.5K OLED Touch, 60Hz");
        addProductDetail(xps15Product, "Graphics",    "NVIDIA GeForce RTX 4060, 8GB GDDR6");
        addProductDetail(xps15Product, "OS",          "Windows 11 Pro");
        addProductDetail(xps15Product, "Ports",       "2x Thunderbolt 4, 1x USB-A, SD Card, 3.5mm Audio");
        createInventory(xps15Product, warehouseDelhi, techDistributors, 25, 5);

        // ── HEADPHONES ───────────────────────────────────────────────────────

        // 4. Sony WH-1000XM5
        Product wh1000 = createProduct(
                "Sony WH-1000XM5 Wireless Headphones",
                "Black", "Industry-leading noise cancelling over-ear headphones with 30-hour battery",
                BigDecimal.valueOf(29990), headphones, sony, wh1000xm5);
        addProductMeasurement(wh1000, weight,     gram, BigDecimal.valueOf(250));
        addProductMeasurement(wh1000, batteryLife, hour, BigDecimal.valueOf(30));
        addProductKeyFeature(wh1000, noiseCancelling, "Industry-leading ANC with 8 microphones");
        addProductKeyFeature(wh1000, battery,         "30 hours (ANC on), 40 hours (ANC off)");
        addProductKeyFeature(wh1000, connectivity,    "Bluetooth 5.2, Multipoint connection (2 devices)");
        addProductDetail(wh1000, "Driver",         "30mm custom driver");
        addProductDetail(wh1000, "Frequency",      "4Hz – 40,000Hz");
        addProductDetail(wh1000, "Quick Charge",   "3 hours playback from 3-minute charge");
        addProductDetail(wh1000, "Folding",        "Non-folding, flat design");
        createInventory(wh1000, warehouseMumbai, techDistributors, 120, 20);

        // 5. Apple AirPods Pro 2nd Gen
        Product airpodsPro = createProduct(
                "Apple AirPods Pro (2nd Generation)",
                "White", "Active noise cancellation earbuds with Adaptive Audio and H2 chip",
                BigDecimal.valueOf(24900), headphones, apple, airpodsProModel);
        addProductMeasurement(airpodsPro, weight,     gram, BigDecimal.valueOf(5.3));
        addProductMeasurement(airpodsPro, batteryLife, hour, BigDecimal.valueOf(6));
        addProductKeyFeature(airpodsPro, noiseCancelling, "Active Noise Cancellation + Transparency Mode");
        addProductKeyFeature(airpodsPro, battery,         "6 hours (ANC on) + 24 hours with case");
        addProductKeyFeature(airpodsPro, connectivity,    "Bluetooth 5.3, H2 chip");
        addProductDetail(airpodsPro, "Chip",            "Apple H2");
        addProductDetail(airpodsPro, "Water Resistance","IPX4 (earbuds), IP54 (case)");
        addProductDetail(airpodsPro, "Find My",         "Precision Finding support");
        addProductDetail(airpodsPro, "Charging",        "Lightning / USB-C / MagSafe");
        createInventory(airpodsPro, warehouseMumbai, apexElectronics, 200, 30);

        // 6. Sony WF-1000XM5
        Product wf1000 = createProduct(
                "Sony WF-1000XM5 True Wireless Earbuds",
                "Black", "Premium true wireless earbuds with world's smallest and lightest ANC",
                BigDecimal.valueOf(21990), headphones, sony, wf1000xm5);
        addProductMeasurement(wf1000, weight,     gram, BigDecimal.valueOf(5.9));
        addProductMeasurement(wf1000, batteryLife, hour, BigDecimal.valueOf(8));
        addProductKeyFeature(wf1000, noiseCancelling, "HD Noise Cancelling Processor QN2e");
        addProductKeyFeature(wf1000, battery,         "8 hours (ANC on) + 24 hours with case");
        addProductKeyFeature(wf1000, connectivity,    "Bluetooth 5.3, Multipoint (2 devices)");
        addProductDetail(wf1000, "Driver",            "8.4mm dynamic driver");
        addProductDetail(wf1000, "Codec",             "LDAC, AAC, SBC");
        addProductDetail(wf1000, "Water Resistance",  "IPX4");
        addProductDetail(wf1000, "Quick Charge",      "60 min playback from 3-minute charge");
        createInventory(wf1000, warehouseDelhi, techDistributors, 90, 15);

        // ── MONITORS ─────────────────────────────────────────────────────────

        // 7. Samsung Odyssey G7
        Product odysseyG7Product = createProduct(
                "Samsung Odyssey G7 32-inch Gaming Monitor",
                "Black", "4K 144Hz curved gaming monitor with 1ms response time and HDMI 2.1",
                BigDecimal.valueOf(69999), monitors, samsung, odysseyG7);
        addProductMeasurement(odysseyG7Product, screenSize, inch, BigDecimal.valueOf(32.0));
        addProductKeyFeature(odysseyG7Product, refreshRate,   "144Hz, 1ms (GtG) response time");
        addProductKeyFeature(odysseyG7Product, connectivity,  "2x HDMI 2.1, 1x DisplayPort 1.4, 2x USB 3.0");
        addProductDetail(odysseyG7Product, "Resolution",      "3840 x 2160 (4K UHD)");
        addProductDetail(odysseyG7Product, "Panel Type",      "VA, 1000R curvature");
        addProductDetail(odysseyG7Product, "HDR",             "DisplayHDR 600");
        addProductDetail(odysseyG7Product, "Sync",            "G-Sync Compatible, FreeSync Premium Pro");
        createInventory(odysseyG7Product, warehouseDelhi, apexElectronics, 35, 5);

        // 8. Dell UltraSharp U2723D
        Product u2723dProduct = createProduct(
                "Dell UltraSharp 27 USB-C Hub Monitor",
                "Black", "4K IPS Black monitor with built-in USB-C hub for professional use",
                BigDecimal.valueOf(59999), monitors, dell, u2723d);
        addProductMeasurement(u2723dProduct, screenSize, inch, BigDecimal.valueOf(27.0));
        addProductKeyFeature(u2723dProduct, refreshRate,   "60Hz, 5ms response time");
        addProductKeyFeature(u2723dProduct, connectivity,  "USB-C 90W, HDMI 2.0, DP 1.4, 4x USB-A");
        addProductDetail(u2723dProduct, "Resolution",      "3840 x 2160 (4K UHD)");
        addProductDetail(u2723dProduct, "Panel Type",      "IPS Black");
        addProductDetail(u2723dProduct, "Color Accuracy",  "Delta E < 2, 100% sRGB");
        addProductDetail(u2723dProduct, "HDR",             "DisplayHDR 400");
        createInventory(u2723dProduct, warehouseMumbai, techDistributors, 20, 4);

        // 9. LG UltraFine 27UN850
        Product lgMonitor = createProduct(
                "LG UltraFine 27UN850 4K Monitor",
                "Silver", "4K UHD IPS monitor with USB-C and Nano IPS technology",
                BigDecimal.valueOf(49999), monitors, lg, lgUltra27);
        addProductMeasurement(lgMonitor, screenSize, inch, BigDecimal.valueOf(27.0));
        addProductKeyFeature(lgMonitor, refreshRate,   "60Hz, 5ms response time");
        addProductKeyFeature(lgMonitor, connectivity,  "USB-C 60W, HDMI 2.0, DisplayPort 1.4");
        addProductDetail(lgMonitor, "Resolution",      "3840 x 2160 (4K UHD)");
        addProductDetail(lgMonitor, "Panel Type",      "Nano IPS");
        addProductDetail(lgMonitor, "Color Gamut",     "98% DCI-P3");
        addProductDetail(lgMonitor, "HDR",             "VESA DisplayHDR 400");
        createInventory(lgMonitor, warehouseDelhi, apexElectronics, 28, 5);

        // ── SMARTPHONES ──────────────────────────────────────────────────────

        // 10. Samsung Galaxy S24 Ultra
        Product s24ultra = createProduct(
                "Samsung Galaxy S24 Ultra",
                "Titanium Black", "Ultimate flagship with S Pen, 200MP camera and AI features",
                BigDecimal.valueOf(134999), smartphones, samsung, galaxyS24Ultra);
        addProductMeasurement(s24ultra, screenSize, inch, BigDecimal.valueOf(6.8));
        addProductMeasurement(s24ultra, weight,     gram, BigDecimal.valueOf(232));
        addProductKeyFeature(s24ultra, processor, "Snapdragon 8 Gen 3 for Galaxy");
        addProductKeyFeature(s24ultra, ram,       "12GB RAM");
        addProductKeyFeature(s24ultra, camera,    "200MP main + 50MP periscope 5x + 10MP 3x + 12MP ultra-wide");
        addProductKeyFeature(s24ultra, battery,   "5000mAh, 45W fast charge");
        addProductDetail(s24ultra, "Display",     "6.8-inch QHD+ Dynamic AMOLED 2X, 120Hz");
        addProductDetail(s24ultra, "Storage",     "256GB / 512GB / 1TB");
        addProductDetail(s24ultra, "OS",          "Android 14 with One UI 6.1");
        addProductDetail(s24ultra, "S Pen",       "Built-in S Pen with 2.8ms latency");
        createInventory(s24ultra, warehouseMumbai, techDistributors, 80, 15);

        // 11. Apple iPhone 15 Pro
        Product iphone15ProProduct = createProduct(
                "Apple iPhone 15 Pro",
                "Natural Titanium", "Pro-grade titanium iPhone with A17 Pro chip and 48MP camera system",
                BigDecimal.valueOf(134900), smartphones, apple, iphone15Pro);
        addProductMeasurement(iphone15ProProduct, screenSize, inch, BigDecimal.valueOf(6.1));
        addProductMeasurement(iphone15ProProduct, weight,     gram, BigDecimal.valueOf(187));
        addProductKeyFeature(iphone15ProProduct, processor, "Apple A17 Pro — 6-core CPU");
        addProductKeyFeature(iphone15ProProduct, ram,       "8GB RAM");
        addProductKeyFeature(iphone15ProProduct, camera,    "48MP main, 12MP ultrawide, 12MP 3x telephoto");
        addProductKeyFeature(iphone15ProProduct, battery,   "3274mAh, USB-C 27W fast charge");
        addProductDetail(iphone15ProProduct, "Display",     "6.1-inch Super Retina XDR, ProMotion 120Hz");
        addProductDetail(iphone15ProProduct, "Storage",     "128GB / 256GB / 512GB / 1TB");
        addProductDetail(iphone15ProProduct, "OS",          "iOS 17");
        addProductDetail(iphone15ProProduct, "Action Button","Customizable Action Button");
        createInventory(iphone15ProProduct, warehouseMumbai, apexElectronics, 100, 20);

        // 12. OnePlus 12
        Product op12Product = createProduct(
                "OnePlus 12",
                "Flowy Emerald", "Flagship performance with Hasselblad camera and 100W SuperVOOC charging",
                BigDecimal.valueOf(64999), smartphones, oneplus, op12);
        addProductMeasurement(op12Product, screenSize, inch, BigDecimal.valueOf(6.82));
        addProductMeasurement(op12Product, weight,     gram, BigDecimal.valueOf(220));
        addProductKeyFeature(op12Product, processor, "Snapdragon 8 Gen 3");
        addProductKeyFeature(op12Product, ram,       "12GB / 16GB LPDDR5X");
        addProductKeyFeature(op12Product, camera,    "50MP Hasselblad main + 48MP ultrawide + 64MP 3x periscope");
        addProductKeyFeature(op12Product, battery,   "5400mAh, 100W SUPERVOOC, 50W AirVOOC");
        addProductDetail(op12Product, "Display",     "6.82-inch 2K+ ProXDR AMOLED, 120Hz LTPO");
        addProductDetail(op12Product, "Storage",     "256GB / 512GB UFS 4.0");
        addProductDetail(op12Product, "OS",          "OxygenOS 14 based on Android 14");
        addProductDetail(op12Product, "Cooling",     "3D Cryo-velocity Vapor Chamber");
        createInventory(op12Product, warehouseDelhi, techDistributors, 70, 10);

        // ── TABLETS ──────────────────────────────────────────────────────────

        // 13. Samsung Galaxy Tab S9
        Product tabS9 = createProduct(
                "Samsung Galaxy Tab S9",
                "Beige", "Premium Android tablet with Dynamic AMOLED display and S Pen included",
                BigDecimal.valueOf(72999), tablets, samsung, galaxyTab9);
        addProductMeasurement(tabS9, screenSize, inch, BigDecimal.valueOf(11.0));
        addProductMeasurement(tabS9, weight,     gram, BigDecimal.valueOf(498));
        addProductKeyFeature(tabS9, processor, "Snapdragon 8 Gen 2 for Galaxy");
        addProductKeyFeature(tabS9, ram,       "8GB RAM");
        addProductKeyFeature(tabS9, storage,   "128GB / 256GB UFS 3.1");
        addProductKeyFeature(tabS9, battery,   "8400mAh, 45W fast charge");
        addProductDetail(tabS9, "Display",     "11-inch Dynamic AMOLED 2X, 120Hz");
        addProductDetail(tabS9, "OS",          "Android 13 with One UI 5.1.1");
        addProductDetail(tabS9, "S Pen",       "Included in box, IP68 rated");
        addProductDetail(tabS9, "Connectivity","Wi-Fi 6E, Bluetooth 5.3, USB-C 3.2");
        createInventory(tabS9, warehouseMumbai, techDistributors, 55, 10);

        // 14. Apple iPad Pro M2
        Product ipadProProduct = createProduct(
                "Apple iPad Pro 11-inch M2",
                "Silver", "Pro tablet with Apple M2 chip, Liquid Retina display and Apple Pencil hover",
                BigDecimal.valueOf(81900), tablets, apple, ipadPro);
        addProductMeasurement(ipadProProduct, screenSize, inch, BigDecimal.valueOf(11.0));
        addProductMeasurement(ipadProProduct, weight,     gram, BigDecimal.valueOf(466));
        addProductKeyFeature(ipadProProduct, processor, "Apple M2 — 8-core CPU, 10-core GPU");
        addProductKeyFeature(ipadProProduct, ram,       "8GB Unified Memory");
        addProductKeyFeature(ipadProProduct, storage,   "128GB / 256GB / 512GB / 1TB / 2TB");
        addProductKeyFeature(ipadProProduct, battery,   "28.65Wh — up to 10 hours");
        addProductDetail(ipadProProduct, "Display",     "11-inch Liquid Retina, ProMotion 120Hz");
        addProductDetail(ipadProProduct, "OS",          "iPadOS 16");
        addProductDetail(ipadProProduct, "Connectivity","Wi-Fi 6E, Bluetooth 5.3, USB4 / Thunderbolt 4");
        addProductDetail(ipadProProduct, "Camera",      "12MP Wide + 10MP Ultra Wide + LiDAR Scanner");
        createInventory(ipadProProduct, warehouseDelhi, apexElectronics, 40, 8);

        // ── COMPUTER ACCESSORIES ─────────────────────────────────────────────

        // 15. Logitech MX Master 3S
        Product mxMaster = createProduct(
                "Logitech MX Master 3S Wireless Mouse",
                "Graphite", "Advanced wireless mouse with 8K DPI, near-silent clicks and MagSpeed scroll wheel",
                BigDecimal.valueOf(9995), accessories, logitech, mxMaster3s);
        addProductMeasurement(mxMaster, weight, gram, BigDecimal.valueOf(141));
        addProductKeyFeature(mxMaster, connectivity, "Bluetooth + USB Receiver (2.4GHz Logi Bolt)");
        addProductKeyFeature(mxMaster, battery,      "70 days on full charge, USB-C quick charge");
        addProductDetail(mxMaster, "DPI",            "200 – 8000 DPI (adjustable)");
        addProductDetail(mxMaster, "Buttons",        "7 buttons + scroll wheel");
        addProductDetail(mxMaster, "Compatibility",  "Windows, macOS, Linux, ChromeOS");
        addProductDetail(mxMaster, "Multi-Device",   "Connect up to 3 devices, Easy-Switch button");
        createInventory(mxMaster, warehouseMumbai, techDistributors, 180, 25);

        // 16. Logitech MX Keys Keyboard
        Product mxKeysProduct = createProduct(
                "Logitech MX Keys Wireless Keyboard",
                "Graphite", "Advanced wireless keyboard with backlit spherical keys and smart illumination",
                BigDecimal.valueOf(8995), accessories, logitech, mxKeys);
        addProductMeasurement(mxKeysProduct, weight, gram, BigDecimal.valueOf(810));
        addProductKeyFeature(mxKeysProduct, connectivity, "Bluetooth + USB Receiver (Logi Bolt), multi-device");
        addProductKeyFeature(mxKeysProduct, battery,      "10 days (backlit), 5 months (no backlight)");
        addProductDetail(mxKeysProduct, "Layout",         "Full-size with numpad");
        addProductDetail(mxKeysProduct, "Backlight",      "Smart proximity sensor, adjustable brightness");
        addProductDetail(mxKeysProduct, "Compatibility",  "Windows, macOS, Linux, iOS, Android");
        addProductDetail(mxKeysProduct, "Multi-Device",   "Connect up to 3 devices");
        createInventory(mxKeysProduct, warehouseDelhi, apexElectronics, 150, 20);

        // 17. Samsung Galaxy Book3 Pro (non-360 variant)
        Product book3ProStd = createProduct(
                "Samsung Galaxy Book3 Pro",
                "Graphite", "Slim and lightweight professional laptop with Dynamic AMOLED display",
                BigDecimal.valueOf(149999), laptops, samsung, galaxyBook3Pro);
        addProductMeasurement(book3ProStd, screenSize, inch, BigDecimal.valueOf(14.0));
        addProductMeasurement(book3ProStd, weight,     gram, BigDecimal.valueOf(1170));
        addProductKeyFeature(book3ProStd, processor, "Intel Core i5-1340P");
        addProductKeyFeature(book3ProStd, ram,       "16GB LPDDR5");
        addProductKeyFeature(book3ProStd, storage,   "512GB NVMe SSD");
        addProductKeyFeature(book3ProStd, battery,   "63Wh — up to 22 hours");
        addProductDetail(book3ProStd, "Display",     "14-inch 3K Dynamic AMOLED 2X, 120Hz");
        addProductDetail(book3ProStd, "Graphics",    "Intel Iris Xe Graphics");
        addProductDetail(book3ProStd, "OS",          "Windows 11 Home");
        addProductDetail(book3ProStd, "Ports",       "2x Thunderbolt 4, 1x USB-A, HDMI 2.0");
        createInventory(book3ProStd, warehouseDelhi, techDistributors, 38, 8);

        // 18. Apple MacBook Pro 16-inch M3 Pro
        Product macbook16 = createProduct(
                "Apple MacBook Pro 16-inch M3 Pro",
                "Silver", "16-inch powerhouse for professionals with M3 Pro chip and 22 hours battery",
                BigDecimal.valueOf(249900), laptops, apple, macbookProM3);
        addProductMeasurement(macbook16, screenSize, inch, BigDecimal.valueOf(16.2));
        addProductMeasurement(macbook16, weight,     gram, BigDecimal.valueOf(2140));
        addProductKeyFeature(macbook16, processor, "Apple M3 Pro — 12-core CPU, 18-core GPU");
        addProductKeyFeature(macbook16, ram,       "18GB Unified Memory");
        addProductKeyFeature(macbook16, storage,   "512GB SSD");
        addProductKeyFeature(macbook16, battery,   "100Wh — up to 22 hours");
        addProductDetail(macbook16, "Display",     "16.2-inch Liquid Retina XDR, ProMotion 120Hz");
        addProductDetail(macbook16, "OS",          "macOS Sonoma");
        addProductDetail(macbook16, "Ports",       "3x Thunderbolt 4, HDMI 2.1, SD Card, MagSafe 3");
        addProductDetail(macbook16, "Camera",      "1080p FaceTime HD, Advanced Image Signal Processor");
        createInventory(macbook16, warehouseMumbai, apexElectronics, 22, 5);

        System.out.println("✅ DummyDataGenerator: Successfully seeded database with:");
        System.out.println("   → " + categoryRepository.count()      + " categories");
        System.out.println("   → " + brandRepository.count()         + " brands");
        System.out.println("   → " + modelRepository.count()         + " models");
        System.out.println("   → " + productRepository.count()       + " products");
        System.out.println("   → " + keyFeaturesRepository.count()   + " key features");
        System.out.println("   → " + measurementTypeRepository.count()+ " measurement types");
        System.out.println("   → " + warehouseRepository.count()     + " warehouses");
        System.out.println("   → " + vendorRepository.count()        + " vendors");
        System.out.println("   → " + inventoryRepository.count()     + " inventory records");
    }
}