package cn.com.xuxiaowei.config;

import cn.com.xuxiaowei.handlerinterceptor.JsEncryptHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器 配置
 *
 * @author xuxiaowei
 */
@Configuration
public class HandlerInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        jsEncryptHandlerInterceptor(registry);

    }

    /**
     * JS（RSA） 加密 拦截器
     */
    private void jsEncryptHandlerInterceptor(InterceptorRegistry registry) {

        // 拦截URL
        List<String> jsEncryptAdd = new ArrayList<>();
        // 排除拦截URL
        List<String> jsEncryptExclude = new ArrayList<>();

        // 注册页面
        jsEncryptAdd.add("/rsa/getPublicKey.do");

        registry.addInterceptor(new JsEncryptHandlerInterceptor())
                .addPathPatterns(jsEncryptAdd)
                .excludePathPatterns(jsEncryptExclude);

    }

}
