package com.tinhvan.hd.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JWTProvider {

    private static Mac sha256HMAC;
    private static long JWT_EXPIRED_TIME_WEB_ADMIN;
    private static long JWT_EXPIRED_TIME_WEB_ESIGN;
    private static long JWT_EXPIRED_TIME_APP;

    /**
     * Init instances
     */
    private static void setKey() {
        try {
            Config config = HDConfig.getInstance();
            JWT_EXPIRED_TIME_WEB_ADMIN = Long.valueOf(config.get("JWT_EXPIRED_TIME_WEB_ADMIN"));
            JWT_EXPIRED_TIME_WEB_ESIGN = Long.valueOf(config.get("JWT_EXPIRED_TIME_WEB_ESIGN"));
            JWT_EXPIRED_TIME_APP = Long.valueOf(config.get("JWT_EXPIRED_TIME_APP"));

            String secret = config.get("JWT_SECRET_KEY");
            SecretKeySpec secretKey = new SecretKeySpec(
                    secret.getBytes(), "HmacSHA256");

            sha256HMAC = Mac.getInstance("HmacSHA256");
            sha256HMAC.init(secretKey);
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    /**
     * Create jwt token
     *
     * @param jwt JWTPayload contain info for generate token
     * @return token created successfully
     */
    public static String encode(JWTPayload jwt) {
        String newJWT;
        setKey();
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] data = mapper.writeValueAsBytes(jwt);
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(data);
            String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            newJWT = header + "." + payload;
            byte[] finalBytes = sha256HMAC.doFinal(
                    newJWT.getBytes(StandardCharsets.UTF_8.name()));
            String hash = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(finalBytes);
            newJWT = newJWT + "." + hash;
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
        return newJWT;
    }

    /**
     * Get value of token
     *
     * @param encoded jwt token
     * @return JWTPayload contain info of jwt token decoded
     */
    public static Pair<Boolean, JWTPayload> decode(String encoded) {
        JWTPayload jwt = new JWTPayload();
        if (HDUtil.isNullOrEmpty(encoded)) {
            return new ImmutablePair<>(true, jwt);
        }
        setKey();
        try {
            String[] tokenPart = encoded.split(" ");
            if (tokenPart.length != 2 || !tokenPart[0].equalsIgnoreCase("BEARER")) {
                return new ImmutablePair<>(false, null);
            }
            String[] tokenData = tokenPart[1].split("\\.");
            if (tokenData.length != 3) {
                return new ImmutablePair<>(false, null);
            }

            String body = tokenData[0] + "." + tokenData[1];

//            byte[] finalBytes = sha256HMAC.doFinal(
//                    body.getBytes(StandardCharsets.UTF_8.name()));
//            String hash = Base64.getUrlEncoder().withoutPadding()
//                    .encodeToString(finalBytes);
//
//            if (!hash.equals(tokenData[2])) {
//                return new ImmutablePair<>(false, null);
//            }


            byte[] payload = Base64.getUrlDecoder().decode(tokenData[1]);
            ObjectMapper mapper = new ObjectMapper();
            jwt = mapper.readValue(payload, JWTPayload.class);

            long expiredTime = jwt.getCreatedAt();
            if (jwt.getEnvironment().equals(HDConstant.ENVIRONMENT.APP))
                expiredTime += JWT_EXPIRED_TIME_APP;
            if (jwt.getEnvironment().equals(HDConstant.ENVIRONMENT.WEB_ESIGN))
                expiredTime += JWT_EXPIRED_TIME_WEB_ESIGN;
            if (jwt.getEnvironment().equals(HDConstant.ENVIRONMENT.WEB_ADMIN))
                expiredTime += JWT_EXPIRED_TIME_WEB_ADMIN;

            if (expiredTime <= HDUtil.getUnixTimeNow()) {
                return new ImmutablePair<>(false, jwt);
            }
        } catch (Exception ex) {
            Log.warn("[JWTProvider][decode]", ex.getMessage());
            return new ImmutablePair<>(false, jwt);
        }
        return new ImmutablePair<>(true, jwt);
    }
}
