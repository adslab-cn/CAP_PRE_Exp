package basic.utils;

import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
*  AESUtil is used to encrypt and decrypt a xml format File,
*  the key is generated from a element in group;
*
* */
public class AESUtil {

    // use AES to encrypt file with Element m as secret key.
    public static byte[] encryptFile(Element m,  byte[] originalData)  {

        SecretKey key = element2AESKey(m);
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(originalData);
        } catch (Exception e) {
            throw new RuntimeException("fail to encrypt", e);
        }

    }

    // use AES to decrypt file with Element m as secret key.
    public static byte[] decryptFile(Element m, byte[] ciphertextBytes)  {
        SecretKey key = element2AESKey(m);

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(ciphertextBytes);
        } catch (Exception e) {
            throw new RuntimeException("fail to decrypt", e);
        }
    }

    // Element to SecretKey
    private static SecretKey element2AESKey(Element e)  {

        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        byte[] hash = Arrays.copyOf(sha256.digest(e.toBytes()), 16);

        return new SecretKeySpec(hash, "AES");
    }

}