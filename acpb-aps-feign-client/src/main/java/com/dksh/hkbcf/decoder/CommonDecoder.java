package com.dksh.hkbcf.decoder;

import com.dksh.hkbcf.exception.exception.ClientException;
import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;


public class CommonDecoder implements Decoder, Encoder {
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
                if(List.of("200").contains(v.get("code").toString())) {
                    return objectMapper.readValue(bytes, objectMapper.constructType(type));
                } else {
                    throw Optional.ofNullable(v.get("msg"))
                            .map(it-> new ClientException(it.toString()))
                            .orElse(new ClientException("Not failure reason found"));
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

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

    }
}
