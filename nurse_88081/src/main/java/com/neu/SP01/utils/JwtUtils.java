package com.neu.SP01.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    //	令牌私有秘钥
    private static final String SECRET="rong123";
    //	定义令牌过期时间的单位
    private static final int calendarField= Calendar.SECOND;
    //	定义令牌的过期时间
    private static final int calendarInterval=1200;

    //	创建令牌的方法
    public static String createToken(String mobileJson){
//		确认令牌签发日期、过期时间
//		签发日期
        Date iaDate=new Date();
//		过期日期
        Calendar nowTime=Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate=nowTime.getTime();
//		确认令牌的类型和加密算法
        Map<String,Object> map=new HashMap<>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
//		结合前两步结果，创建令牌字符串
        String token= JWT.create()
                .withHeader(map)
                .withClaim("mobileJson",mobileJson)
                .withIssuedAt(iaDate)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }
    //	解密令牌的方法
    private static Map<String, Claim> verifyToken(String token){
        DecodedJWT jwt=null;
        JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
        jwt=verifier.verify(token);
        return jwt.getClaims();
    }

    //	把令牌解析成json的方法
    public static String parseToken(String token){
        Map<String,Claim> claims=verifyToken(token);
        Claim mobileClaim=claims.get("mobileJson");

        return mobileClaim.asString();
    }
    public static boolean validateToken(String token, RedisTemplate<String, String> redisTemplate) {
        try {
            // 1. 验证JWT是否过期
            Map<String, Claim> claims = verifyToken(token);
            String userJson = claims.get("mobileJson").asString(); // 包含用户信息的JSON

            // 2. 解析JSON，动态获取用户ID（兼容userId和customerId）
            JSONObject userInfo = JSON.parseObject(userJson);
            String userId = userInfo.getString("userId");
            String customerId = userInfo.getString("customerId");

            // 3. 检查Redis中是否存在该Token（优先检查userId，不存在则检查customerId）
            String redisKey = (userId != null) ? userId : customerId;
            if (redisKey == null) {
                return false; // JSON中既无userId也无customerId
            }

            String redisToken = redisTemplate.opsForValue().get(redisKey);
            return token.equals(redisToken);
        } catch (Exception e) {
            return false; // JWT过期、解析失败或Redis查询异常
        }
    }

}