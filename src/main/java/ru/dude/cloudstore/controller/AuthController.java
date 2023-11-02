package ru.dude.cloudstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dude.cloudstore.controller.docs.annotation.AuthTokenParameter;
import ru.dude.cloudstore.dto.AuthRequest;
import ru.dude.cloudstore.model.TokenResponse;
import ru.dude.cloudstore.service.AuthWithJWTService;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication manager")
public class AuthController {
    private AuthWithJWTService authWithJWTService;

    @Operation(description = "Authorization method")
    @ApiResponse(responseCode = "200", description = "Success authorization",
            content = @Content(mediaType = APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(ref = "#/components/schemas/Login")))
    @ApiResponse(responseCode = "400", description = "Bad credentials",
            content = @Content(mediaType = APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(ref = "#/components/schemas/Error")))
    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid AuthRequest authRequest) {
        return authWithJWTService.login(authRequest);
    }

    @Operation(description = "Logout")
    @ApiResponse(responseCode = "200", description = "Success logout",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @AuthTokenParameter
    @PostMapping("/logout")
    public void logout() {
    }
}
