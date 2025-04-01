package com.dksh.hkbcf.decoder;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;


public class ICKDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == 404 || response.status() == 204)
            return Util.emptyValueOf(type);
        if (response.body() == null)
            return null;
        try{
            final var bytes = response.body().asInputStream().readAllBytes();
            final var v =objectMapper.readValue(bytes, HashMap.class);
            if(v.containsKey("code")) {
                if(List.of("0").contains(v.get("code").toString())) {
                    return objectMapper.readValue(bytes, objectMapper.constructType(type));
                } else {
                    throw Optional.ofNullable(v.get("message"))
                            .map(it-> new RuntimeException(it.toString()))
                            .orElse(new RuntimeException("Not failure reason found"));
                }
            } else {
                throw new RuntimeException("fail");
            }

        }
        catch (Throwable ex){
            throw new RuntimeException(ex.getMessage());
        }


    }
}
