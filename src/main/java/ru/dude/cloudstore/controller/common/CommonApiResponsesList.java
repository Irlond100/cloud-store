package ru.dude.cloudstore.controller.common;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.dude.cloudstore.dto.ErrorResponse;
import ru.dude.cloudstore.model.FileResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized error.",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "200", description = "Success deleted",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error input data.",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface CommonApiResponsesList {
}
