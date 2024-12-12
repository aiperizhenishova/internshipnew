package kg.alatoo.taskmanagementsystem.services;

import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendResetEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            // Генерация токен
            String token = UUID.randomUUID().toString(); // Уникальный токен


            // Установка времени жизни токена (например, 15 минут)
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);


            // Сохраняем токен и время жизни в базе данных
            UserEntity user = userOptional.get();
            user.setResetToken(token);
            user.setResetTokenExpiryDate(expiryDate);
            userRepository.save(user);

            // Генерация ссылки для сброса пароля
            String resetUrl = "http://localhost:8080/auth/change-password?token=" + token;

            // Отправка email
            sendEmail(email, resetUrl);
        }
    }

    private void sendEmail(String toEmail, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: " + resetUrl);
        mailSender.send(message);
    }



}
