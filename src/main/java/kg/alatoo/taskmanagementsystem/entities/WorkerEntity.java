package kg.alatoo.taskmanagementsystem.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "worker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String fullName;
    private LocalDateTime modifiedAt;

    @Email
    @Column(unique = true)
    private String email;
}
