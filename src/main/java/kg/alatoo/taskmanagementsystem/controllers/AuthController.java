package kg.alatoo.taskmanagementsystem.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email, Password, and Username are required");
        }

        try {
            // Проверка на существующего пользователя
            if (userRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword())); // Если требуется шифрование пароля
            userRepository.save(user);  // сохраняет пользователя в базу
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();  // Выводим ошибку в консоль для диагностики
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserEntity user) {
        UserEntity existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Сравнение паролей, используя BCryptPasswordEncoder
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Генерация токена
        String token = generateToken(existingUser.getUsername());

        return ResponseEntity.ok("Login successful. Token: " + token);
    }

    // Генерация токена
    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Генерация токена сброса
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);

            // Отправка email со ссылкой для сброса пароля
            String resetUrl = "http://localhost:8080/auth/change-password?token=" + resetToken;
            sendResetEmail(email, resetUrl);

            return ResponseEntity.ok("Reset email sent.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
        }
    }

    private void sendResetEmail(String email, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click the following link to reset your password: " + resetUrl);
        mailSender.send(message);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        Optional<UserEntity> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Проверка на истечение времени жизни токена
            if (user.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
            }

            // Если токен действителен, обновляем пароль
            user.setPassword(newPassword); //  шифрование пароля, если требуется
            user.setResetToken(null); // Удалить токен после сброса
            user.setResetTokenExpiryDate(null); // Удаляем дату истечения
            userRepository.save(user);

            return ResponseEntity.ok("Password successfully changed.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
}
