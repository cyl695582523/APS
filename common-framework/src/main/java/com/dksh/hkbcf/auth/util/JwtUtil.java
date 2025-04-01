package com.dksh.hkbcf.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;

public class JwtUtil {


    private final static String issuer = "dksh";
    private final static String secretKey = "secretKey";
    private final static String timeZone = "Asia/Hong_Kong";
    private final static Duration expiration = Duration.ofHours(2);
    public record JwtPair(String accessToken, Instant expiresIn) {}

    public static JwtPair generate(String username, List<String> roles) {
        final var now = Instant.now().atZone(TimeZone.getTimeZone(timeZone).toZoneId());
        final var expire = now.plus(expiration);
        final String jwt = JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now.toInstant())
                .withExpiresAt(now.plus(expiration).toInstant())
                .withSubject(username)
                .withClaim("roles", roles)
//                .sign(Algorithm.RSA256(publicKey, privateKey))
                .sign(Algorithm.HMAC512(secretKey))
                ;

        return new JwtPair(jwt, expire.toInstant());
    }
    public static DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .withIssuer(issuer)
                .build()
                .verify(token);
    }

    public static boolean verify(String token) {
        try{
            getDecodedJWT(token);
            return true;
        } catch (Exception ex){
            return false;
        }
    }

}
