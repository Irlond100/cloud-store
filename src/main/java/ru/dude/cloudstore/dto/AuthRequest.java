package ru.dude.cloudstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotEmpty
    @JsonProperty(HeaderNameHolder.USER_HEADER_NAME)
    private String username;
    @Size(min = 4, max = 64, message = "password:  must be at least 4 characters and contain at least one digit and one uppercase letter")
    @NotEmpty
    private String password;
}
