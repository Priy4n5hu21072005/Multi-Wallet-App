package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    public RedisServiceImpl(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    private static final Duration otpExpiry = Duration.ofMinutes(5);
    @Override
    public void savePendingUsers(RegisterUserRequest request, String otp) {
        String key = "register:"+request.getEmail();
        Map<String,Object>data = new HashMap<>();
        data.put("fullName",request.getFullName());
        data.put("email",request.getEmail());
        data.put("phone",request.getPhoneNumber());
        data.put("password",request.getPassword());
        data.put("Otp",otp);

        redisTemplate.opsForValue().set(key,data,otpExpiry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegisterUserRequest getPendingUsers(String email) {
        String key = "register:"+ email;
        Map<String,Object>data = (Map<String, Object>) redisTemplate.opsForValue().get(key);

        if(data==null) return null;
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName((String) data.get("fullName"));
        request.setEmail((String) data.get("email"));
        request.setPhoneNumber((String) data.get("phone"));
        request.setPassword((String) data.get("password"));

        return request;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getOtp(String email) {
        String key = "register:"+email;
        Map<String,Object>data=(Map<String, Object>) redisTemplate.opsForValue().get(key);
        if (data==null)return null;

        return (String) data.get("otp");
    }

    @Override
    public void deletePendingUser(String email) {
        redisTemplate.delete("register:"+email);

    }
}
