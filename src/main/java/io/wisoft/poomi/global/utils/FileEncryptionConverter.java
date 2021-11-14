package io.wisoft.poomi.global.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Component
public class FileEncryptionConverter {

    private SecretKey key;
    private Cipher cipher;

    public FileEncryptionConverter() {
        try {
            this.key = KeyGenerator.getInstance("AES").generateKey();
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }

}
