package cn.com.xuxiaowei.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            map.put("msg", "成功获取到 session 中的 publicKey");
            data.put("publicKey", publicKey);
        }

        return map;
    }

}
