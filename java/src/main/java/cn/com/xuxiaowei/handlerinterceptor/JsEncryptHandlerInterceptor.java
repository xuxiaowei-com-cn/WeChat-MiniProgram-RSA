package cn.com.xuxiaowei.handlerinterceptor;

import cn.com.xuxiaowei.util.rsa.RsaUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * JS（RSA） 加密 拦截器
 *
 * @author xuxiaowei
 */
public class JsEncryptHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            // 初始化密钥
            Map<String, Key> stringKeyMap = RsaUtils.initKey();

            // 公钥
            String publicKey = RsaUtils.getPublicKey(stringKeyMap);

            // 私钥
            String privateKey = RsaUtils.getPrivateKey(stringKeyMap);

            HttpSession session = request.getSession();

            session.setAttribute("publicKey", publicKey);
            session.setAttribute("privateKey", privateKey);

        } catch (NoSuchAlgorithmException e) {

            System.err.println("初始化密钥失败！");

            e.printStackTrace();

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
