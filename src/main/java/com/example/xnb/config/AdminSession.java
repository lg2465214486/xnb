package com.example.xnb.config;

import cn.hutool.core.util.ObjectUtil;
import com.example.xnb.entity.XNBUser;
import com.example.xnb.mapper.XNBUserMapper;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Component
public class AdminSession {

    @Autowired
    private RedisTemplate<String, XNBUser> redisTemplate;
    @Autowired
    private XNBUserMapper userMapper;
    private static final String SESSION_KEY = "session:admin:";
    private static AdminSession instance;

    public AdminSession() {
        instance = this;
    }

    public static AdminSession getInstance() {
        return instance;
    }

    public XNBUser admin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        if (ObjectUtil.isEmpty(token)) {
            token = request.getParameter("X-Token");
        }
        return redisTemplate.opsForValue().get(SESSION_KEY + token);
    }

    public String setAdmin(XNBUser user) {
        if (user == null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String token = request.getHeader("X-Token");
            redisTemplate.delete(SESSION_KEY + token);
            return "";
        } else {
            XNBUser userSession = new XNBUser();
            BeanUtils.copyProperties(user, userSession);
            userSession.setPwd(null);
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(SESSION_KEY + token, userSession, 30, TimeUnit.MINUTES);
            return token;
        }
    }

    public String logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("X-Token");
        XNBUser user = redisTemplate.opsForValue().get(SESSION_KEY + token);
        if (null != user)
            redisTemplate.delete(SESSION_KEY + token);
        return "logout success";
    }

    public String updateAdmin(XNBUser user) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String token = request.getHeader("X-Token");
            user.setPwd(null);
            redisTemplate.opsForValue().set(SESSION_KEY + token, user, 30, TimeUnit.MINUTES);
            return token;
    }

    public String updateAdmin(String token, XNBUser user) {
        user.setPwd(null);
        redisTemplate.opsForValue().set(SESSION_KEY + token, user, 30, TimeUnit.MINUTES);
        return token;
    }
}