package basic.utils;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;


public class AESUtilBackup {

    /**
     * 生成AES 128位密钥
     * @return 密钥字节数组 (16 bytes)
     */
    private static byte[] generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密钥生成失败", e);
        }
    }

    /**
     * AES加密（ECB模式）
     * @param plaintextBytes 明文字节数组
     * @param key 密钥字节数组 (必须16字节)
     * @return 加密后的原始字节数组
     */
    public static byte[] encrypt(byte[] plaintextBytes, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(plaintextBytes);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * AES解密（ECB模式）
     * @param ciphertextBytes 密文字节数组
     * @param key 密钥字节数组 (必须16字节)
     * @return 解密后的原始字节数组
     */
    public static byte[] decrypt(byte[] ciphertextBytes, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(ciphertextBytes);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * XML文件测试用例
     */
    @Test
    public void textXML() throws Exception {
        // 1. 读取XML文件
        Path xmlPath = Paths.get("params/HL7.xml");
        byte[] originalData = Files.readAllBytes(xmlPath);
        System.out.println("原始文件大小: " + originalData.length + " bytes");

        // 2. 生成密钥
        byte[] key = generateKey();
        System.out.println("密钥长度: " + key.length + " bytes");

        // 3. 执行加密
        byte[] encryptedData = encrypt(originalData, key);
        System.out.println("加密后大小: " + encryptedData.length + " bytes");

        // 4. 执行解密
        byte[] decryptedData = decrypt(encryptedData, key);
        System.out.println("解密后大小: " + decryptedData.length + " bytes");

        // 5. 验证数据一致性
        boolean isConsistent = java.util.Arrays.equals(originalData, decryptedData);
        System.out.println("数据验证: " + (isConsistent ? "成功" : "失败"));

        // 6. 保存解密结果
        if (isConsistent) {
            Path outputPath = Paths.get("decrypted.xml");
            Files.write(outputPath, decryptedData);
            System.out.println("解密文件已保存至: " + outputPath.toAbsolutePath());
        }
    }
}