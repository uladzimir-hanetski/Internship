package org.example.userserv.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CardRequest {
    @NotBlank(message = "Number can't be blank")
    @Size(max = 32, message = "Number should be less than 32 characters")
    private String number;

    @NotBlank(message = "Holder can't be blank")
    @Size(max = 32, message = "Holder should be less than 32 characters")
    private String holder;

    @NotNull(message = "Expiration date can't be null")
    @Future(message = "Incorrect date")
    private LocalDate expirationDate;

    @NotNull(message = "User id can't be null")
    @Positive(message = "User id should be positive number")
    private Long userId;
}
