package com.example.xnb.config;

import com.example.xnb.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String SESSION_KEY = "session:admin:";

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*").allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/account/**","/qwertyuiop/user/add", "/pub/**","/multiFile/**", "/line/**", "/coin/**");
    }

    public class AdminHandlerInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            try {
                if (!request.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.toString())) {
                    User user = AdminSession.getInstance().admin();
                    if (null == user) {
                        throw new Exception("login failure");
                    } else {
                        String token = request.getHeader("X-Token");
                        redisTemplate.opsForValue().set(SESSION_KEY + token, user, 30, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception e) {
                log.error("sys error: " + e);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    out.append("{\"code\": 401, \"message\": \"" + e.getMessage() + "\"}");
                } catch (IOException ex) {
                    log.error("" + ex);
                    out.close();
                }
                return false;
            }
            return true;
        }
    }
}
