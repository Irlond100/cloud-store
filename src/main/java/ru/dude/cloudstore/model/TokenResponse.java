package ru.dude.cloudstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dude.cloudstore.dto.HeaderNameHolder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @JsonProperty(HeaderNameHolder.TOKEN_HEADER_NAME)
    private String authToken;
}
