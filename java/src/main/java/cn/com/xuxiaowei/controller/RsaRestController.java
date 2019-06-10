package cn.com.xuxiaowei.controller;

import cn.com.xuxiaowei.util.rsa.RsaUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 非对称性加密 RestController
 *
 * @author xuxiaowei
 */
@RestController
@RequestMapping("/rsa")
public class RsaRestController {

    /**
     * 获取公钥
     */
    @RequestMapping("/getPublicKey.do")
    public Map<String, Object> getPublicKey(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>(4);
        Map<String, Object> data = new HashMap<>(4);
        map.put("data", data);

        HttpSession session = request.getSession();

        String publicKey = (String) session.getAttribute("publicKey");

        if (StringUtils.isEmpty(publicKey)) {
            map.put("code", 0);
            map.put("msg", "session 中无 publicKey");
        } else {
            map.put("code", 0);
            map.put("msg", "成功获取到 session 中的 RSA publicKey");
            data.put("publicKey", publicKey);
        }

        return map;
    }

    /**
     * 解密
     *
     * @param text 需要解密的数据
     */
    @RequestMapping("/decrypt.do")
    public Map<String, Object> decrypt(HttpServletRequest request, HttpServletResponse response, String text) {

        Map<String, Object> map = new HashMap<>(4);
        Map<String, Object> data = new HashMap<>(4);
        map.put("data", data);

        HttpSession session = request.getSession();

        String privateKey = (String) session.getAttribute("privateKey");

        if (StringUtils.isEmpty(privateKey)) {
            map.put("code", 0);
            map.put("msg", "session 中无 privateKey");
        } else {

            System.err.println("解密前的字符串为："+ text);

            try {

                String decryptText = new String(RsaUtils.decryptByPrivateKey(text, privateKey));

                System.err.println("解密后的字符串为："+ decryptText);

                map.put("code", 0);
                map.put("msg", "成功解密");

                data.put("text", decryptText);

            } catch (GeneralSecurityException e) {

                map.put("code", 1);
                map.put("msg", "成功失败");

                return map;

                // <code>e.printStackTrace();</code>
            }

        }

        return map;
    }

}
