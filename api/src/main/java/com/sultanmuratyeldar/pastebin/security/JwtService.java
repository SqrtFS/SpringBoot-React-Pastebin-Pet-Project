    package com.sultanmuratyeldar.pastebin.security;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

    import java.security.Key;
    import java.util.Base64;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Function;
    import org.springframework.beans.factory.annotation.Value;
    import java.util.Set;


    @Service
    public class JwtService {

        @Value("${JWT_SECRET}")
        private String SECRET_KEY;

        public String extractUsername(String jwtToken) {
            return extractClaim(jwtToken , Claims::getSubject);
        }
        private Claims extractAllClaims(String jwtToken){
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build().parseClaimsJws(jwtToken)
                    .getBody();
        }

        public String generateToken(UserDetails userDetails){
            return  generateToken(new HashMap<>() , userDetails);
        }
        public String generateToken(Map<String , Object> extraClaims , UserDetails userDetails){
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .signWith(getSignKey() , SignatureAlgorithm.HS256)
                    .compact();
        }

        public String generateToken(UserDetails userDetails, Set<String> roles) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("roles", roles);
            return generateToken(extraClaims, userDetails);
        }
        public boolean isTokenValid(String jwtToken , UserDetails userDetails){
            final String username = extractUsername(jwtToken);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
        }
        public boolean isTokenExpired(String jwtToken){
         return extractExpiration(jwtToken).before(new Date());
        }

        private Date extractExpiration(String jwtToken) {
            return extractClaim(jwtToken , Claims::getExpiration);
        }


        public <T> T extractClaim(String jwtToken , Function<Claims , T> claimsResolver){
            final Claims claims = extractAllClaims(jwtToken);
            return claimsResolver.apply(claims);
        }

        private Key getSignKey() {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }
