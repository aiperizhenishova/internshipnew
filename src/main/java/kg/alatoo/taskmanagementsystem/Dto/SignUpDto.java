package kg.alatoo.taskmanagementsystem.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpDto {

    @NotEmpty
    @Size(min = 4, max = 15)
    String username;

    @NotEmpty
    @Size(min = 5)
    String password;

    @NotEmpty
    String name;
}
