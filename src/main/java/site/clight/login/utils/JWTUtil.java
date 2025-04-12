package site.clight.login.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类，提供token的生成、验证和解析功能
 */
@Component
public class JWTUtil {

    @Value("${jwt.key-value}")
    private String keyValue; // JWT签名密钥

    @Value("${jwt.expiration}")
    private Long expiration; // token过期时间(毫秒)

    /**
     * 生成HMAC-SHA签名密钥
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(keyValue.getBytes());
    }

    /**
     * 生成JWT token
     * @param userId 用户ID
     * @return 生成的token字符串
     */
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, userId);
    }

    /**
     * 创建JWT token
     * @param claims token中携带的声明信息
     * @param subject token主题(通常为用户ID)
     * @return 生成的token字符串
     */
    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证token有效性
     * @param token 待验证的token
     * @return 验证结果(true-有效, false-无效)
     */
    public Boolean verifyToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (Exception ex) {
            System.out.println("Invalid JWT token");
        }
        return false;
    }

    /**
     * 从token中提取指定声明信息
     * @param token JWT token
     * @param claimsResolver 声明解析函数
     * @return 解析后的声明信息
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中提取用户ID
     * @param token JWT token
     * @return 用户ID
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从token中提取过期时间
     * @param token JWT token
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 解析token中的全部声明信息
     * @param token JWT token
     * @return Claims对象
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }
}
