package kg.alatoo.taskmanagementsystem.controllers;

import kg.alatoo.taskmanagementsystem.Dto.AdminDto;
import kg.alatoo.taskmanagementsystem.Dto.SuccessDto;
import kg.alatoo.taskmanagementsystem.Dto.WorkerDto;
import kg.alatoo.taskmanagementsystem.entities.AdminEntity;
import kg.alatoo.taskmanagementsystem.entities.TaskEntity;
import kg.alatoo.taskmanagementsystem.entities.WorkerEntity;
import kg.alatoo.taskmanagementsystem.exceptions.ApiException;
import kg.alatoo.taskmanagementsystem.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/get-all")
    public List<AdminEntity> getAll(@RequestParam(value = "name", required = false) String name){
        if (name != null){
            return adminRepository.findAllByNameContainingIgnoreCase(name);
        }else {
            return adminRepository.findAll();
        }
    }

    @GetMapping("get/{id}")
    public AdminEntity getById (@PathVariable("id") Long id){
        return adminRepository.findById(id).orElseThrow(() -> new ApiException("Admin" + id + "not found", HttpStatusCode.valueOf(404)));
    }


    @PostMapping("/create")
    public AdminEntity create (@RequestBody AdminEntity adminEntity){
        return adminRepository.save(adminEntity);
    }


    @PutMapping("/update/{id}")
    public AdminEntity update(@RequestBody AdminDto adminDto, @PathVariable Long id){
        AdminEntity toUpdate = adminRepository.findById(id).get();
        if (adminDto.getName() != null){
            toUpdate.setName(adminDto.getName());
        }

        if (adminDto.getUsername() != null){
            toUpdate.setUsername(adminDto.getUsername());
        }

        if (adminDto.getPassword() != null){
            toUpdate.setPassword(adminDto.getPassword());
        }

        if (adminDto.getName() != null){
            toUpdate.setName(adminDto.getName());
        }

        if (adminDto.getCreatedAt() != null){
            toUpdate.setCreatedAt(adminDto.getCreatedAt());
        }
        return adminRepository.save(toUpdate);
    }

    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        adminRepository.deleteById(id);
        return new SuccessDto(true);
    }

}

