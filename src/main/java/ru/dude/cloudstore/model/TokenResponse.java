package ru.dude.cloudstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dude.cloudstore.dto.HeaderNameHolder;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Schema(name = "auth-token", description = "Contains json web token")
public class TokenResponse {
    @JsonProperty(HeaderNameHolder.TOKEN_HEADER_NAME)
    private String authToken;
}
