package kg.alatoo.taskmanagementsystem.controllers;


import kg.alatoo.taskmanagementsystem.Dto.WorkerDto;
import kg.alatoo.taskmanagementsystem.entities.WorkerEntity;
import kg.alatoo.taskmanagementsystem.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping("/get-all")
    public List<WorkerEntity> getAll(@RequestParam(value = "name", required = false) String name){
        if (name != null){
            return workerRepository.findAllByFullNameContainingIgnoreCase(name);
        }else {
            return workerRepository.findAll();
        }
    }

    @PostMapping("/create")
    public WorkerEntity create (@RequestBody WorkerEntity workerEntity){
        return workerRepository.save(workerEntity);
    }


    @PutMapping("/update/{id}")
    public WorkerEntity update(@RequestBody WorkerDto workerDto, @PathVariable Long id){
        WorkerEntity toUpdate = workerRepository.findById(id).get();

        if (workerDto.getFullName() != null){
            toUpdate.setFullName(workerDto.getFullName());
        }

        if (workerDto.getEmail() != null){
            toUpdate.setEmail(workerDto.getEmail());
        }

        toUpdate.setModifiedAt(LocalDateTime.now());
        return workerRepository.save(toUpdate);
    }




}
