package kg.alatoo.taskmanagementsystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public class JwtTokenService {

    @Service
    public class JwtTokenService {

        private String secretKey = "your_secret_key";

        // Метод для генерации токена
        public String generateToken(String email) {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Токен действителен 1 час
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }
}
