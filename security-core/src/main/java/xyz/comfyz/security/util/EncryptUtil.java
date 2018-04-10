package xyz.comfyz.security.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        16:33 2018/3/28
 * Version:     1.0
 * Description:
 */
public class EncryptUtil {
    private static final byte[] DFT_AES_KEY = new byte[]{-13, -111, -42, -1, 50, 31, 74, 2, -28, -120, 37, -112, 114, -77, -89, 17};
    private static final byte[] DFT_AES_IV = new byte[]{21, 51, 33, -97, -74, -36, -81, -40, -44, 55, 95, -107, 19, -28, 114, -35};
    private static final int SALT_LENGTH = 16;
    private static final String ENCODING = "utf-8";
    private static final String AES_CIPHER = "AES/CBC/PKCS5Padding";

    public EncryptUtil() {
    }

    public static String aesEncrypt(String plainText) {
        return aesEncrypt(plainText, DFT_AES_KEY);
    }

    public static String aesEncrypt(String plainText, String key) {
        Objects.requireNonNull(key, "arg key");
        return aesEncrypt(plainText, getBytes(key));
    }

    private static String aesEncrypt(String plainText, byte[] key) {
        Objects.requireNonNull(plainText, "arg plainText");
        Objects.requireNonNull(key, "arg key");

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, new SecretKeySpec(key, "AES"), new IvParameterSpec(DFT_AES_IV));
            byte[] plainBytes = getBytes(plainText);
            byte[] saltBytes = genSaltBytes();
            byte[] plainAllBytes = addAll(plainBytes, saltBytes);
            byte[] bytes = cipher.doFinal(plainAllBytes);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception var7) {
            throw new IllegalArgumentException(var7);
        }
    }

    public static String aesDecrypt(String encryptedText) {
        return aesDecrypt(encryptedText, DFT_AES_KEY);
    }

    public static String aesDecrypt(String encryptedText, String key) {
        Objects.requireNonNull(key, "arg key");
        return aesDecrypt(encryptedText, getBytes(key));
    }

    private static String aesDecrypt(String encryptedText, byte[] key) {
        Objects.requireNonNull(encryptedText, "arg encryptedText");
        Objects.requireNonNull(key, "arg key");

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, new SecretKeySpec(key, "AES"), new IvParameterSpec(DFT_AES_IV));
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] plainBytes = cipher.doFinal(encryptedBytes);
            return new String(plainBytes, 0, plainBytes.length - 16, "utf-8");
        } catch (Exception var5) {
            throw new IllegalArgumentException(var5);
        }
    }

    private static byte[] getBytes(String s) {
        try {
            return s.getBytes("utf-8");
        } catch (Exception var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    private static byte[] genSaltBytes() {
        Random random = new Random();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String md5(String text) {
        return hash("MD5", text);
    }

    public static String sha256(String text) {
        return hash("SHA-256", text);
    }

    public static String hash(String algorithm, String text) {
        Objects.requireNonNull(algorithm, "algorithm can't be null");
        Objects.requireNonNull(text, "text can't be null");

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] bytes = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            byte[] var5 = bytes;
            int var6 = bytes.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                byte b = var5[var7];
                String hex = Integer.toHexString(255 & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }

                sb.append(hex);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var10) {
            throw new IllegalArgumentException(var10);
        }
    }

    public static String transfer(byte[] sinData) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 6; ++i) {
            int x;
            int z;
            int y;
            if (i == 5) {
                x = sinData[5] & 3;
                z = sinData[5] & 3;
                y = sinData[5] & 3;
                buffer.append(x + z + y);
            } else {
                x = sinData[i * 3] & 3;
                z = sinData[i * 3 + 1] & 3;
                y = sinData[i * 3 + 2] & 3;
                buffer.append(x + z + y);
            }
        }

        return buffer.toString();
    }

    public static byte[] addAll(byte[] array1, byte... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            byte[] joinedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static byte[] clone(byte[] array) {
        return array == null ? null : (byte[]) array.clone();
    }
}
