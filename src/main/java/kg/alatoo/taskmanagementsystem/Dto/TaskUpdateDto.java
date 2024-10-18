package kg.alatoo.taskmanagementsystem.Dto;

import kg.alatoo.taskmanagementsystem.entities.TaskStatus;
import kg.alatoo.taskmanagementsystem.entities.WorkerEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskUpdateDto {

    String title;

    String description;

    LocalDateTime deadline;

    TaskStatus status;

    WorkerEntity worker;


}
