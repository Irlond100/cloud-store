package ru.dude.cloudstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dude.cloudstore.dto.AuthRequest;
import ru.dude.cloudstore.model.TokenResponse;
import ru.dude.cloudstore.service.AuthWithJWTService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication manager")
@ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = APPLICATION_JSON_VALUE)
)

public class AuthController {
    private AuthWithJWTService authWithJWTService;

    @Operation(description = "Returns a token for a valid user")
    @ApiResponse(responseCode = "200", description = "Successful authentication",
            content = @Content(mediaType = APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Invalid credentials",
            content = @Content(mediaType = APPLICATION_JSON_VALUE))

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        return authWithJWTService.login(authRequest);
    }

    @Operation(description = "Logout user")
    @ApiResponse(responseCode = "200", description = "Valid token",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "401", description = "Invalid token or Unauthorized",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))

    @PostMapping("logout")
    public void logout() {
    }
}
