package kg.alatoo.taskmanagementsystem.controllers;


import kg.alatoo.taskmanagementsystem.Dto.LoginDto;
import kg.alatoo.taskmanagementsystem.Dto.SignUpDto;
import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.Dto.UserDto;
import kg.alatoo.taskmanagementsystem.entities.EntriesEntity;
import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.UserRepository;
import kg.alatoo.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /*
    @GetMapping("/{userId}/favorites")
    public List<EntriesEntity> getFavorites(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavorites(); // возвращаем избранные записи
    }
     */



    @GetMapping("/get-all")
    public List<UserEntity> getAll(@RequestParam(value = "name", required = false) String name){
        if (name != null){
            return userRepository.findAllByUsernameContainingIgnoreCase(name);
        }else {
            return userRepository.findAll();
        }
    }

    @GetMapping("get/{id}")
    public UserEntity getById (@PathVariable("id") Long id){
        return userRepository.findById(id).orElseThrow(() -> new ApiException("User" + id + "not found", HttpStatusCode.valueOf(404)));
    }

    @PostMapping("/create")
    public UserEntity create (@RequestBody UserEntity userEntity){
        return userRepository.save(userEntity);
    }



    @PutMapping("/update/{id}")
    public UserEntity update(@RequestBody UserDto userDto, @PathVariable Long id){
        UserEntity toUpdate = userRepository.findById(id).get();

        if (userDto.getUsername() != null){
            toUpdate.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null){
            toUpdate.setEmail(userDto.getEmail());
        }

        toUpdate.setModifiedAt(LocalDateTime.now());
        return userRepository.save(toUpdate);
    }


    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new SuccessDto(true);
    }

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpDto signUpDto) {
        userService.saveUser(signUpDto);
        return ResponseEntity.ok("User registered successfully");
    }

    // Логин пользователя
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(token);
    }



}
