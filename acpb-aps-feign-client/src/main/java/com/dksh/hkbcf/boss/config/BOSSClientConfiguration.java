package com.dksh.hkbcf.boss.config;

import com.dksh.hkbcf.exception.exception.ClientException;
import feign.*;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;

//public class BOSSClientConfiguration implements Decoder, RequestInterceptor {
public class BOSSClientConfiguration implements Decoder{

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == 404 || response.status() == 204)
            return Util.emptyValueOf(type);
        if (response.body() == null)
            return null;
        try{
            final var bytes = response.body().asInputStream().readAllBytes();
            final var v =objectMapper.readValue(bytes, HashMap.class);
            if(v.containsKey("resultCode")) {
                if(List.of("1").contains(v.get("resultCode").toString())) {
                    return objectMapper.readValue(bytes, objectMapper.constructType(type));
                } else {
                    throw Optional.ofNullable(v.get("resultMsg"))
                            .map(it-> new ClientException(it.toString()))
                            .orElse(new ClientException("CPVACS record not found"));
                }
            } else {
//                throw new RuntimeException("fail");
                return objectMapper.readValue(bytes, objectMapper.constructType(type));
            }

        }
        catch (Throwable ex){
            throw new ClientException(ex.getMessage());
        }


    }/*

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", String.format("Bearer %s", "6122e102-11c3-4f0a-b690-8cc7f64c925f"));
    }
*/
}
