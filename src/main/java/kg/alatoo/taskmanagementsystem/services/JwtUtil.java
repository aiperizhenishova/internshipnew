package kg.alatoo.taskmanagementsystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
public class JwtUtil {

    // Метод для генерации JWT
    public static String generateToken(String username) {
        // Создаем безопасный ключ для алгоритма HS256
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Генерация токена
        return Jwts.builder()
                .setSubject(username)  // можно добавить другие данные, например, роли
                .signWith(key)  // Подписываем токен
                .compact();  // Завершаем построение токена
    }

    // Метод для извлечения данных из токена (например, username)
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Метод для извлечения любого поля из токена
    private static <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Метод для извлечения всех данных из токена
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS256)) // тот же ключ
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Метод для проверки срока действия токена
    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new java.util.Date());
    }

    // Метод для извлечения времени истечения токена
    private static java.util.Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Метод для валидации токена
    public static boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}