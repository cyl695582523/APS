package com.dksh.hkbcf.mps.config;

import com.dksh.hkbcf.exception.exception.ClientException;
import com.dksh.hkbcf.exception.exception.MPSClientException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.dksh.hkbcf.util.ObjectMapperUtil.objectMapper;

public class MPSClientConfiguration implements Decoder {

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
//                if(List.of("200").contains(v.get("code").toString()) && response.status()==200) {
                if(response.status()==200) {
                    return objectMapper.readValue(bytes, objectMapper.constructType(type));
                } else {
                    /*
                        private Integer status = 0;
                        private Integer code = 0;
                        private String message = "error";
                        private Object data = null;
                     */

                    throw new MPSClientException(response.status(),
                            Integer.parseInt(v.get("code").toString()),
                            v.get("msg")==null?"MPS failure":v.get("msg").toString(),
                            v.get("data")
                            );
/*
                    throw Optional.ofNullable(v.get("msg"))
                            .map(it-> new MPSClientException(it.toString(), response.status(), v.get("code").toString()))
                            .orElse(new ClientException("MPS failure", response.status(), v.get("code").toString()));
                    */
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
}
