package com.ml.hotel_ml_apigateway_service.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class EncryptorUtil {

    private final IvParameterSpec iv;
    private final SecretKeySpec skeySpec;
    private final String algo;

    public EncryptorUtil(
            @Value(value = "${encryptor.key}") String key,
            @Value(value = "${encryptor.init.vector}") String initVector,
            @Value(value = "${encryptor.algo}") String algo) {
        this.iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        this.skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        this.algo = algo;
    }

    public String encrypt(String value) throws Exception {
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(original);
    }

}
