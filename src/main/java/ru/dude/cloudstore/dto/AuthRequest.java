package ru.dude.cloudstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthRequest (
        @NotEmpty
        @JsonProperty("login")
        String username,
        @Size(min = 4, max = 64, message = "password:  must be at least 4 characters and contain at least one digit and one uppercase letter")
        @NotEmpty
        String password
) {
}
