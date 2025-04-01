package com.dksh.hkbcf.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperUtil {

    public final static ObjectMapper objectMapper = new ObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    //objectMapper.registerModule(new JavaTimeModule());
//    private final static Logger log = Logger.getLogger("ObjectMapperUtil");


    public static <T1,T2> T2 clone(T1 objectValue, Class<T2> objectClass) {

        try {
            T2 result = objectMapper.readValue(objectMapper.writeValueAsString(objectValue), objectClass);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
