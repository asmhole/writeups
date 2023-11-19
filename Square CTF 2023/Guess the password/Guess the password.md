# Guess the password (150 pts) - reversing

## Description

Once you give it the password, it will show you the flag!

## Attachments

[Authenticator.apk](Authenticator.apk)

## Solution

Decompiling the APK yields an imposing 37 MB folder. As you can guess, searching for the string "FLAG" shows us that only one of these files is useful to us. Extracting out the Android app configuration and reformatting the code gives us:
```java
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
/* loaded from: classes3.dex */
public class GuessThePassword {
    static String SHA256 = "5dfd1ac9741873dbb889fc5f6362af39c7e8085ea3d952974f37ca66e6f6c597";
    static String FLAG = "9YU3fxDFo276KmxJ4FbfZJYldNY9K4xHRkEjSjeh1hlBWGgYr4CRGo2+w4bHKIL7";
    static int screenWidth = 1080;
    static int screenHeight = 2205;

    public static void main(String[] args) {
        String password = "";
        if (isPassword(screenWidth, screenHeight, password)) {
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

    public static boolean isPassword(int a, int b, String password) {
        byte[] data = shuffle(a, b, getHash(password));
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

    public static byte[] shuffle(int seedA, int seedB, byte[] secret) {
        Random randA = new Random(seedA * seedB);
        for (int i = 0; i < secret.length; i++) {
            int randomIndex = randA.nextInt(secret.length);
            byte temp = secret[i];
            secret[i] = secret[randomIndex];
            secret[randomIndex] = temp;
        }
        return secret;
    }

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
```
At first, this may look like a worrysome sight, but a closer look reveals `isPassword` only calls `shuffle`, `getHash`, and `binToHex`. The other crypto functions are called once you have the correct password hash to AES decrypt `FLAG` to reveal our answer, made significantly easier with the observation that `generateAESKeyFromPassword` is a onw-way function because it doesn't initialize `salt`.

Before directly checking for hash equality, `isPassword` first shuffles the password's hash using a seeded randomizer. Upon closer inspection, the seed is constant, and thus the shuffler will always randomize the input the same way. Applying this deterministic tranformation in reverse gives us the correct password's hash.

We aren't done yet; `getFlag` expects the raw password, so we have to reverse the correct password's hash. A simple SHA1 lookup gives us the incredibly cimactic `SQSQSQ` as the password.

Inputting this password successfully decrypts the flag `flag{yeah_i_use_google_pixel_6a}` (use ios loser)