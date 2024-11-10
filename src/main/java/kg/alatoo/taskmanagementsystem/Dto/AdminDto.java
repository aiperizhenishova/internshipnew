package kg.alatoo.taskmanagementsystem.Dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminDto {

    private Long id;

    private String name;

    private String username;

    private String password;

    LocalDateTime createdAt;

}
