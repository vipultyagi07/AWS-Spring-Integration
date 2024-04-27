package vip.secrets;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypto {


    public static String transform(String encryptedPassword, String secretKey) throws Exception {
        // Generate a secret key from the provided secretKey string
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 65536, 256); // Adjust parameters as needed
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        // Decode the Base64 string to get the encrypted bytes
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);

        // Decrypt the encrypted bytes
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes back to a string
        return new String(decryptedBytes, "UTF-8");
    }

}
