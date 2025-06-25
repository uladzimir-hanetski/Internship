package org.example.userserv.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRequest {
    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 32, message = "Name should be between 2 and 32 characters")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @Size(min = 2, max = 64, message = "Surname should be between 2 and 64 characters")
    private String surname;

    @NotNull(message = "Birth date can't be null")
    @Past(message = "Incorrect date")
    private LocalDate birthDate;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Incorrect email format")
    @Size(max = 64, message = "Email should be less than 64 characters")
    private String email;
}