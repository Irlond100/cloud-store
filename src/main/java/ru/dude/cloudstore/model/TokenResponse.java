package ru.dude.cloudstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.dude.cloudstore.dto.HeaderNameHolder;

public record TokenResponse(
        @JsonProperty(HeaderNameHolder.TOKEN_HEADER_NAME)
        String authToken
) {
}
