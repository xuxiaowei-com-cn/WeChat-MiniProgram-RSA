package cn.com.xuxiaowei.util.rsa;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 非对称加密 工具类
 * <p>
 * 依赖 commons-codec 包
 *
 * @author xuxiaowei
 */
public class RsaUtils {

    /**
     *
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     *
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * Map 储存的 公钥名称
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * Map 储存的 私钥名称
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥
     *
     * @return Map 集合中 类型的密钥对
     * @throws NoSuchAlgorithmException 当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                  如果没有Provider支持指定算法的KeyFactorySpi实现。
     */
    public static Map<String, Key> initKey() throws NoSuchAlgorithmException {

        // 根据指定算法，生成公钥/私钥对，返回KeyPairGenerator对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);

        // 使用特定密钥大小初始化密钥对生成器
        keyPairGenerator.initialize(1024);

        // 生成密钥对。
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 创建指定长度的 Map
        Map<String, Key> keyMap = new HashMap<>(2);

        // 将公钥和私钥储存在 Map 中
        // 公钥
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());
        // 私钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());

        return keyMap;
    }

    /**
     * 将 byte 数组 编码为 Base 64 字符串
     *
     * @param bytes byte 数组
     * @return Base 64 字符串
     */
    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }


    /**
     * 将 String 解码 Base 64 数组
     *
     * @param key String
     * @return Base 64 数组
     */
    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return 数字签名
     * @throws NoSuchAlgorithmException 当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                  如果没有Provider支持指定算法的KeyFactorySpi实现。
     * @throws InvalidKeySpecException  无效密钥
     * @throws InvalidKeyException      无效密钥（无效编码，错误长度，未初始化等）
     * @throws SignatureException       签名异常
     */
    public static String sign(byte[] data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回 true 失败返回 false
     * @throws NoSuchAlgorithmException 当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                  如果没有Provider支持指定算法的KeyFactorySpi实现。
     * @throws InvalidKeySpecException  无效密钥
     * @throws InvalidKeyException      无效密钥（无效编码，错误长度，未初始化等）
     * @throws SignatureException       签名异常
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }


    /**
     * 通过私钥解密
     *
     * @param data 需要私密解密的数据
     * @param key  私密
     * @return 使用私密解密后的数据 byte 类型
     * @throws NoSuchPaddingException    安全异常
     * @throws NoSuchAlgorithmException  当请求特定加密算法但在环境中不可用时，抛出此异常。
     * @throws IllegalBlockSizeException 安全异常
     * @throws BadPaddingException       安全异常
     * @throws InvalidKeyException       无效密钥（无效编码，错误长度，未初始化等）
     * @throws InvalidKeySpecException   无效密钥
     * @throws GeneralSecurityException  总异常
     */
    public static byte[] decryptByPrivateKey(String data, String key) throws GeneralSecurityException {
        return decryptByPrivateKey(decryptBASE64(data), key);
    }


    /**
     * 通过公钥解密
     *
     * @param data 需要公钥解密的数据
     * @param key  公钥
     * @return 使用公钥解密后的数据
     * @throws NoSuchPaddingException    安全异常
     * @throws NoSuchAlgorithmException  当请求特定加密算法但在环境中不可用时，抛出此异常。
     * @throws InvalidKeySpecException   无效密钥
     * @throws InvalidKeyException       无效密钥（无效编码，错误长度，未初始化等）
     * @throws BadPaddingException       安全异常
     * @throws IllegalBlockSizeException 安全异常
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Map<String, Object> map = publicKey(key);
        Cipher cipher = (Cipher) map.get("cipher");
        Key publicKey = (Key) map.get("publicKey");

        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 通过公钥加密
     *
     * @param data 需要公钥加密的数据
     * @param key  公钥
     * @return 使用公钥加密后的数据
     * @throws NoSuchPaddingException    安全异常
     * @throws NoSuchAlgorithmException  当请求特定加密算法但在环境中不可用时，抛出此异常。
     * @throws InvalidKeySpecException   无效密钥
     * @throws InvalidKeyException       无效密钥（无效编码，错误长度，未初始化等）
     * @throws BadPaddingException       安全异常
     * @throws IllegalBlockSizeException 安全异常
     */
    public static byte[] encryptByPublicKey(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Map<String, Object> map = publicKey(key);
        Cipher cipher = (Cipher) map.get("cipher");
        Key publicKey = (Key) map.get("publicKey");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }


    /**
     * 公钥加密解密相关
     *
     * @param key 公钥
     * @return Key + Cipher
     * @throws NoSuchAlgorithmException 当请求特定加密算法但在环境中不可用时，抛出此异常。
     * @throws InvalidKeySpecException  无效密钥
     * @throws NoSuchPaddingException   安全异常
     */
    public static Map<String, Object> publicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {

        Map<String, Object> map = new HashMap<>(2);

        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        String algorithm = keyFactory.getAlgorithm();
        Cipher cipher = Cipher.getInstance(algorithm);

        map.put("publicKey", publicKey);
        map.put("cipher", cipher);

        return map;
    }

    /**
     * 通过私钥解密
     *
     * @param data 需要私密解密的数据
     * @param key  私密
     * @return 私密解密后的数据 byte 类型
     * @throws NoSuchPaddingException    安全异常
     * @throws NoSuchAlgorithmException  当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                   如果没有Provider支持指定算法的KeyFactorySpi实现。
     * @throws InvalidKeySpecException   无效密钥
     * @throws InvalidKeyException       无效密钥（无效编码，错误长度，未初始化等）
     * @throws BadPaddingException       安全异常
     * @throws IllegalBlockSizeException 安全异常
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Map<String, Object> map = privateKey(key);
        Cipher cipher = (Cipher) map.get("cipher");
        Key privateKey = (Key) map.get("privateKey");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 通过私钥加密
     *
     * @param data 需要私钥加密的数据
     * @param key  私钥
     * @return 私钥加密后的数据 byte 类型
     * @throws NoSuchPaddingException    安全异常
     * @throws NoSuchAlgorithmException  当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                   如果没有Provider支持指定算法的KeyFactorySpi实现。
     * @throws InvalidKeySpecException   无效密钥
     * @throws InvalidKeyException       无效密钥（无效编码，错误长度，未初始化等）
     * @throws BadPaddingException       安全异常
     * @throws IllegalBlockSizeException 安全异常
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Map<String, Object> map = privateKey(key);
        Cipher cipher = (Cipher) map.get("cipher");
        Key privateKey = (Key) map.get("privateKey");

        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 私钥加密/解密相关
     *
     * @throws NoSuchAlgorithmException 当请求特定加密算法但在环境中不可用时，抛出此异常。
     *                                  如果没有Provider支持指定算法的KeyFactorySpi实现。
     * @throws InvalidKeySpecException  无效的密钥
     *                                  如果给定的密钥规范不适合此密钥工厂生成私钥。
     * @throws NoSuchPaddingException   安全异常
     */
    public static Map<String, Object> privateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {

        Map<String, Object> map = new HashMap<>(2);

        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        String algorithm = keyFactory.getAlgorithm();
        Cipher cipher = Cipher.getInstance(algorithm);

        map.put("privateKey", privateKey);
        map.put("cipher", cipher);

        return map;
    }


    /**
     * 取得私钥
     *
     * @param keyMap 放置公钥/私钥的 Map 集合
     * @return 私钥
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap 放置公钥/私钥的 Map 集合
     * @return 公钥
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static void main(String[] args) {

        try {

            Map<String, Key> stringKeyMap = initKey();

            // 公钥
            String publicKey = getPublicKey(stringKeyMap);
            System.out.println("公钥：" + publicKey);
            System.out.println();

            // 私钥
            String privateKey = getPrivateKey(stringKeyMap);
            System.out.println("私钥：" + privateKey);
            System.out.println();

            // Scanner sc = new Scanner(System.in);

            /* String next = sc.next(); */

            String data = "今天去上课吗？";

            System.out.println("需要加密的数据：" + data);

            byte[] b = encryptByPublicKey(data, publicKey);

            System.out.println();

            byte[] bytes = decryptByPrivateKey(b, privateKey);

            String s = new String(bytes);
            System.out.println("解密后的数据：" + s);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
