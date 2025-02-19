    package com.example.backendquanlibanhang.security.jwt;

    import com.example.backendquanlibanhang.security.userprincal.UserPrinciple;
    import io.jsonwebtoken.*;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Component;

    import java.util.Date;
    @Component
    public class JwtProvider {
        private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
        @Value("${jwt.secret}")
        private String jwtSecret ;
        @Value("${jwt.expiration}")
        private int jwtExpiration;

        public String createToken(Authentication authentication){
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            return Jwts.builder().setSubject(userPrinciple.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime()+jwtExpiration*1000))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }
        public boolean validateToken(String token){
            if (token == null || token.isEmpty()) {
                return false;
            }
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                return true;
            } catch (SignatureException e){
                logger.error("Invalid JWT signature -> Message: {}",e);
            } catch (MalformedJwtException e){
                logger.error("Invalid format Token -> Message: {}",e);
            } catch (ExpiredJwtException e){
                logger.error("Expired JWT token -> Message: {}",e);
            } catch (UnsupportedJwtException e){
                logger.error("Unsupported JWT token -> Message: {}",e);
            } catch (IllegalArgumentException e){
                logger.error("JWT claims string is empty --> Message {}",e);
            }
            return false;
        }
        public String getUerNameFromToken(String token){
            String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
            return userName;
        }
    }
