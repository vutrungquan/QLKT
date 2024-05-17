package mng.qlkt.security.jwt;

import mng.qlkt.security.userprical.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String jwtSecret = "hethongquanlykhothuoc.vn";
    private int jwtExpiration = 86400;

    public String createToken(Authentication authentication){
        UserPrinciple userPrinciple =(UserPrinciple) authentication.getPrincipal();
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration *1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e) {
            logger.error("Invalid JWT singnature -> Message: {}", e);
        }catch (MalformedJwtException e){
            logger.error("Invalid format token -> Message:{}", e);
        }catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message:{}", e);
        }catch (UnsupportedJwtException e) {
            logger.error("Unsupport JWT token -> Message:{}", e);
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is emty -> Message:{}",e);
        }
        return false;
    }
    public String getUserNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}
