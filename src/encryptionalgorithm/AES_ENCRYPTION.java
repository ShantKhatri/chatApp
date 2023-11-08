package encryptionalgorithm;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES_ENCRYPTION {

    private static final String key = "HemPraDivAnkAbhN"; // 16 characters for AES-128

    public static String encrypt(String plainText) {
        try {
//            System.out.println("Encrypt Mujhe bulaya gaya tha");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
//            System.out.println("Decrypt Mujhe bulaya gaya tha");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            return new String(decryptedTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


//package encryptionalgorithm;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.GCMParameterSpec;
//import java.util.Base64;
//
//public class AES_ENCRYPTION {
//    private static SecretKey key;
//
//    private static final int KEY_SIZE = 128;
//    private static final int DATA_LENGTH = 128;
//    private static Cipher encryptionCipher;
//
//
//    static {
//        try {
//            init();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public static void init() throws Exception {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(KEY_SIZE);
//        key = keyGenerator.generateKey();
//        System.out.println(key);
//    }
//
//    public static String encrypt(String data) throws Exception {
//        byte[] dataInBytes = data.getBytes();
//        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
//        encryptionCipher.init(Cipher.ENCRYPT_MODE,key);
//        byte[] encryptedBytes = encryptionCipher.doFinal(dataInBytes);
//        return encode(encryptedBytes);
//    }
//
//    public static String decrypt(String encryptedData) throws Exception {
//        byte[] dataInBytes = decode(encryptedData);
//        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
//        GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, encryptionCipher.getIV());
//        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
//        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);
//        return new String(decryptedBytes);
//    }
//
//    private static String encode(byte[] data) {
//        return Base64.getEncoder().encodeToString(data);
//    }
//
//    private static byte[] decode(String data) {
//        return Base64.getDecoder().decode(data);
//    }
//}
