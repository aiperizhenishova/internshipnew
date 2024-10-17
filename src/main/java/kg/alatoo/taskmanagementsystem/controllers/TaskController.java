package kg.alatoo.taskmanagementsystem.controllers;


import kg.alatoo.taskmanagementsystem.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("get/{id}")
    public TaskEntity getById (@PathVariable("id") Long id){
        return taskRepository.findById(id).orElseThrow(() -> new ApiException("Task" + id + "not found", HttpStatusCode.valueOf(404)));
    }

    @GetMapping("/get-all")
    public List<TaskEntity> getAll(){
        return taskRepository.findAll();
    }

    @PostMapping("/create")
    public TaskEntity create(@RequestBody TaskEntity newTask){
        return taskRepository.save(newTask);
    }


    @PutMapping("update/{id}")
    public TaskEntity update(@RequestBody TaskUpdateDto task, @PathVariable("id") Long id){
        TaskEntity toUpdate = taskRepository.findById(id).get();

        if (task.getName() != null){
            toUpdate.setName(task.getName());
        }

        if (task.getTitle() != null){
            toUpdate.setTitle(task.getTitle());
        }


        if (task.getDescription() != null){
            toUpdate.setDescription(task.getDescription());
        }

        if (task.getCreatedAt() != null){
            toUpdate.setCreatedAt(task.getCreatedAt());
        }

        if (task.getDeadline() != null){
            toUpdate.setDeadline(task.getDeadline());
        }

        return taskRepository.save(toUpdate);

    }

    @DeleteMapping("delete/{id}")
    public SuccessDto delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return new SuccessDto(true);
    }

}
