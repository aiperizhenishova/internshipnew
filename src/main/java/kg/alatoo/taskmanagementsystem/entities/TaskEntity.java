package kg.alatoo.taskmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD
=======
import org.springframework.boot.autoconfigure.security.SecurityProperties;
>>>>>>> 32087394a541ad60dc2bd793e134aa9d5b48532f

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
<<<<<<< HEAD
    private WorkerEntity user;
=======
    private UserEntity user;
>>>>>>> 32087394a541ad60dc2bd793e134aa9d5b48532f
}
