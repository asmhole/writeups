import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class GuessThePassword {
    // static String SHA256 = "5dfd1ac9741873dbb889fc5f6362af39c7e8085ea3d952974f37ca66e6f6c597";
    static String SHA256 = "8966d9fd5ddb97635e5fe8e697af4f62b8a3c7fcc939ca37f67408c5731a1852";
    static String FLAG = "9YU3fxDFo276KmxJ4FbfZJYldNY9K4xHRkEjSjeh1hlBWGgYr4CRGo2+w4bHKIL7";
    public static void main(String[] args) {
        String password = "SQSQSQ";
        if (isPassword(password)) {
            System.out.println("Password is correct!");
            try {
                System.out.println(getFlag(password));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("Incorrect password!");
    }

    /**
newL = [4, 3, 29, 20, 25, 30, 28, 5, 16, 0, 19, 9, 7, 15, 13, 21, 18, 10, 26, 8, 17, 2, 31, 12, 14, 23, 22, 1, 11, 24, 27, 6]
org = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]
sha = "5dfd1ac9741873dbb889fc5f6362af39c7e8085ea3d952974f37ca66e6f6c597".match(/../g)
newHash = new Array(32)
for (let i = 0; i < 32; i++) {
    newHash[newL[i]] = sha[i]
}
     */

    public static boolean isPassword(String password) {
        byte[] hash = getHash(password);
        byte[] data = hash;
        String dataStr = bin2hex(data);
        System.out.println("Shuffled : " + dataStr);
        return dataStr.equals(SHA256);
    }

    public static String getFlag(String password) throws Exception {
        SecretKey secretKey = generateAESKeyFromPassword(password);
        return decrypt(FLAG, secretKey);
    }

    public static byte[] getHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }

    static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data) {
            hex.append(String.format("%02x", Integer.valueOf(b & 255)));
        }
        return hex.toString();
    }

    // public static byte[] shuffle(byte[] secret) {
    //     Random randA = new Random(2381400);
    //     for (int i = 0; i < secret.length; i++) {
    //         int randomIndex = randA.nextInt(secret.length);
    //         byte temp = secret[i];
    //         secret[i] = secret[randomIndex];
    //         secret[randomIndex] = temp;
    //     }
    //     return secret;
    // }

    public static SecretKey generateAESKeyFromPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = factory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
