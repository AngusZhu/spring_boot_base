package com.sinosafe.payment.common;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TokenUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TokenUtil.class);
    public static final String MAGIC_KEY = "obfuscatedpayment";
    public static final String AES_KEY = "HUA_AN_PAYMENT";

    public static void main(String[] args) {
        System.out.println(TokenUtil.getUUIDToken("12345678901234567890123456789012345678901234567890", "Windows"));
    }

    private static String generateDESKey() {
        String part1 = DigestUtils.md5DigestAsHex(AES_KEY.getBytes());
        String part2 = DigestUtils.md5DigestAsHex(MAGIC_KEY.getBytes());
        return part1 + part2.substring(8);
    }

    public static String getToken(String clientInfo) {
/* Expires in one hour */
        long minsToExpires = 60 * 1;
        long expiration = System.currentTimeMillis() + 1000L * 60 * minsToExpires;
        clientInfo = EncryptUtil.AESencode(clientInfo, AES_KEY);
        return TokenUtil.getToken(clientInfo, expiration, minsToExpires);
    }

    public static String getUUIDToken(String userId, String device) {
        userId = EncryptUtil.DESencode(userId, generateDESKey());
        return TokenUtil.getToken(userId, device, UUID.randomUUID().toString().replace("-", ""));
    }

    public static String getToken(String clientInfo, String device, String uuid) {
        Long timestamp = System.currentTimeMillis();
        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(clientInfo);
        tokenBuilder.append(":");
        tokenBuilder.append(timestamp);
        tokenBuilder.append(":");
        tokenBuilder.append(device);
        tokenBuilder.append(":");
        tokenBuilder.append(uuid);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtil.computeSignature(clientInfo, timestamp, uuid));
        return tokenBuilder.toString();
    }

    public static String getToken(String clientInfo, Long expiration, Long minsToExpires) {
        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(clientInfo);
        tokenBuilder.append(":");
        tokenBuilder.append(expiration);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtil.computeSignature(clientInfo, expiration));
        tokenBuilder.append(":");
        tokenBuilder.append(minsToExpires);
        return tokenBuilder.toString();
    }

    public static String computeSignature(String clientInfo, long expiration) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(clientInfo);
        signatureBuilder.append(":");
        signatureBuilder.append(expiration);
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtil.MAGIC_KEY);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public static String computeSignature(String clientInfo, long timestamp, String uuid) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(clientInfo);
        signatureBuilder.append(":");
        signatureBuilder.append(timestamp);
        signatureBuilder.append(":");
        signatureBuilder.append(uuid);
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtil.MAGIC_KEY);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public static boolean validateToken(String authToken) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];
        if (expires < System.currentTimeMillis()) {
            LOG.warn("Auth-Token expired!");
            return false;
        }
        boolean valid = signature.equals(TokenUtil.computeSignature(parts[0], expires));
        if (!valid) {
            LOG.warn("Auth-Token has been changed!");
        }
        return valid;
    }

    public static boolean validateUUIDToken(String authToken) {
        String[] parts = authToken.split(":");
        if (parts.length != 5) {
            return false;
        }
        String uuid = parts[3];
        String signature = parts[4];
        boolean valid = signature.equals(TokenUtil.computeSignature(parts[0], Long.parseLong(parts[1]), uuid));
        if (!valid) {
            LOG.warn("Auth-Token has been changed!");
        }
        return valid;
    }

    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }
        String[] parts = authToken.split(":");
        return EncryptUtil.DESdecode(parts[0], generateDESKey());
    }
}