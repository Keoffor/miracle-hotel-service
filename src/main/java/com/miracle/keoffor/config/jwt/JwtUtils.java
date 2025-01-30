package com.miracle.keoffor.config.jwt;

import com.miracle.keoffor.config.user_auth.HotelUserDetails;
import com.miracle.keoffor.constants.JwtCredConstant;
import com.miracle.keoffor.constants.RoleConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.token.expirationTime}")
    private int jwtExpirationTime;

    public String generateJwtTokenForUser(Authentication authentication){
        HotelUserDetails userPrincipal = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        Map<String,Object> rolemap = new HashMap<>();
            rolemap.put("role", roles);
            return createTheToken(rolemap, userPrincipal.getUsername(),userPrincipal.getFirstName());

    }

    private String createTheToken(Map<String, Object> claims, String userName, String firstName) {
        claims.put("issu","www.kenstudy.com");
        claims.put("principal", firstName);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtCredConstant.SECRET.getJwtCred());
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    private String extractUsernameFromToken(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody().getSubject();
//    }
    public String extractUserNameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Date extractTokenExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token){
        return extractTokenExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        try {
            final String userName = extractUserNameFromToken(token);
            if (userName.equals(userDetails.getUsername()) && !isTokenExpired(token))
                return true;
        }catch (MalformedJwtException e){
                logger.error("Invalid jwt token : {}", e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Expired token : {} ", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("This token is not supported: {} ", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("No Claims found : {} ", e.getMessage());
        }
        return false;
    }


}
