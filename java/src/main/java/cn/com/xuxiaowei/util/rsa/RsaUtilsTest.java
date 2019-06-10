package cn.com.xuxiaowei.util.rsa;

import java.security.Key;
import java.util.Map;

/**
 * 测试 RSA 非对称性加密
 *
 * @author xuxiaowei
 */
public class RsaUtilsTest {

    private static String publicKey;
    private static String privateKey;

    public static void main(String[] args) throws Exception {

        setUp();

        System.out.println();

        test();

        System.out.println();

        testSign();

        System.out.println();
    }

    public static void setUp() throws Exception {
        Map<String, Key> keyMap = RsaUtils.initKey();
        publicKey = RsaUtils.getPublicKey(keyMap);
        privateKey = RsaUtils.getPrivateKey(keyMap);
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
    }

    public static void test() throws Exception {
        System.out.println("公钥加密——私钥解密");
        String inputStr = "今天中午吃啥呢？？？";
        byte[] encodedData = RsaUtils.encryptByPublicKey(inputStr, publicKey);
        byte[] decodedData = RsaUtils.decryptByPrivateKey(encodedData,
                privateKey);
        String outputStr = new String(decodedData);
        System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
//        assertEquals(inputStr, outputStr);
    }

    public static void testSign() throws Exception {
        System.out.println("私钥加密——公钥解密");
        String inputStr = "快上课了。。。";
        byte[] data = inputStr.getBytes();
        byte[] encodedData = RsaUtils.encryptByPrivateKey(data, privateKey);
        byte[] decodedData = RsaUtils.decryptByPublicKey(encodedData, publicKey);
        String outputStr = new String(decodedData);
        System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
//        assertEquals(inputStr, outputStr);
        System.out.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RsaUtils.sign(encodedData, privateKey);
        System.out.println("签名:" + sign);
        // 验证签名
        boolean status = RsaUtils.verify(encodedData, publicKey, sign);
        System.out.println("状态:" + status);
    }

}
