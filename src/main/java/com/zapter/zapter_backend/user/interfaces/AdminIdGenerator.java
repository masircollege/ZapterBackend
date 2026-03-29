//package com.zapter.zapter_backend.user.interfaces;
//
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.generator.BeforeExecutionGenerator;
//import org.hibernate.generator.EventType;
//
//import java.util.EnumSet;
//
//public class AdminIdGenerator implements BeforeExecutionGenerator {
//
//    //adm-MHAK98345
//
//    @Override
//    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType){
////        Long value = ((Number) session.
////                createNativeQuery("select firstname,lastname from employee where role = 'SYSTEM_ADMIN'").
////                getSingleResult())
////                .longValue();
//
//    return null;
//    }
//
//
//    @Override
//    public EnumSet<EventType> getEventTypes(){
//        return EnumSet.of(EventType.INSERT);
//    }
//}
