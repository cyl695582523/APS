package com.dksh.hkbcf.cpvacs.config;

import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.service.CPVACSAuthService;
import com.dksh.hkbcf.exception.exception.ClientException;
import feign.*;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;

@Configuration
//public class CPVACSClientConfiguration implements Decoder, RequestInterceptor {
public class CPVACSClientConfiguration implements Decoder{

/*    @Autowired
    CPVACSAuthService cpvacsAuthService;*/

//    private final CPVACSAuthService cpvacsAuthService;

    CPVACSAuthClient cpvacsAuthClient;

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {

        // abnormal header
        if (response.status() == 404 || response.status() == 204)
            throw new ClientException("CPVACS record not found");
//            return Util.emptyValueOf(type);
        if (response.body() == null)
            throw new ClientException("CPVACS record not found");
//            return null;

        // normal header
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


    }
/*
    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", String.format("Bearer %s",
                "testing"));
//        template.header("Authorization", String.format("Bearer %s",
//                cpvacsAuthService.login()));
    }
    */

}
