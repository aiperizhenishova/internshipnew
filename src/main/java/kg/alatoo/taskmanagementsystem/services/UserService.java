package kg.alatoo.taskmanagementsystem.services;

import kg.alatoo.taskmanagementsystem.Dto.SignUpDto;
import kg.alatoo.taskmanagementsystem.entities.FavoriteEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.model.UserModel;
import kg.alatoo.taskmanagementsystem.repositories.FavoriteRepository;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;


@Service
@Slf4j
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder bCryptPassword;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    //Этот метод нужен Spring Security, чтобы знать, кто залогинился.
    //Если нашёл — отдаёт объект с данными о юзере (email, пароль, доп. информация).
    //Если не нашёл — кидает ошибку: "Этого email нет в базе!".
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Поиск пользователя по email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email" + email + " not found"));

        return new UserModel(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getModifiedAt()
        );
    }


    //login Ищет юзера по email, проверяет пароль, если ок — всё норм и генерит jwt token, иначе кидает исключение
    public String loginUser(String email, String password) {
        // Поиск пользователя по email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Проверка пароля
        if (!bCryptPassword.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Генерация токена
        return jwtTokenProvider.createToken(userEntity.getEmail() /*userEntity.getRoles()*/);
    }



    // register Метод для регистрации пользователя(Проверяет email, шифрует пароль, сохраняет юзера в базе.)
    public void saveUser(SignUpDto signUpDto) {
        log.info("Sign up user: {}", signUpDto.getUsername());

        // Создаем новый объект пользователя
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signUpDto.getUsername());
        userEntity.setPassword(bCryptPassword.encode(signUpDto.getPassword()));
        userEntity.setEmail(signUpDto.getEmail());
        userEntity.setModifiedAt(signUpDto.getModifiedAt());

        try {
            userRepository.save(userEntity);     // сохраняем пользователя в базе
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("User with email " + signUpDto.getEmail() + " is already exists", HttpStatusCode.valueOf(409));
        } catch (Exception e) {
            log.error("Error occurred while saving user", e);
            throw new ApiException("Error while user creating", HttpStatusCode.valueOf(400));
        }
    }

}
