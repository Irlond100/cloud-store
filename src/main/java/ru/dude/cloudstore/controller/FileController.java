package ru.dude.cloudstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dude.cloudstore.dto.ErrorResponse;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.service.FileServiceImpl;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileServiceImpl fileServiceImpl;
    private final Map<String, Resource> oneTimeLinks = new HashMap<>();

    @Operation(description = "Download file from cloud")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "400", description = "Error input data.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized error.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error delete file",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))

    @AuthTokenParameter
    @GetMapping("/file")
    public ResponseEntity<String> getFileLinkKey(
            @Parameter(description = "Name of the file to generate one-time download link for")
            @RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        final var fileResource = fileServiceImpl.getFileResource(fileRequest);
        String linkKey = generateRandomLinkKey();
        oneTimeLinks.put(linkKey, fileResource);
        return ResponseEntity.ok().body(linkKey);
    }

    @Operation(description = "Deletes a file")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "400", description = "Error input data.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized error.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error delete file",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))

    @AuthTokenParameter
    @DeleteMapping("/file")
    public String deleteFile(@RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        return fileServiceImpl.deleteFile(fileRequest);
    }

    @Operation(description = "Uploads a file")
    @ApiResponse(responseCode = "200", description = "Success upload",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "400", description = "Error input data.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized error.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))

    @AuthTokenParameter
    @PostMapping("file")
    public String handleFileUpload(
            @ModelAttribute @Valid FileUploadRequest fileUploadRequest) throws RuntimeException, IOException {
        return fileServiceImpl.upload(fileUploadRequest);
    }

    @Operation(description = "Get all files")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Error input data.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized error.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error delete file",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))

    @AuthTokenParameter
    @GetMapping("list")
    public List<FileResponse> limitListUploaded(
            @RequestParam("limit") @Min(1) @Valid int limit) throws RuntimeException, IOException {
        return fileServiceImpl.getFileInfoList(limit);
    }

    @Operation(description = "Edit file name")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "400", description = "Error input data.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized error.",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error delete file",
            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))

    @AuthTokenParameter
    @PutMapping("/file")
    public String handleFileRename(
            @Parameter(description = "Filename to rename", required = true)
            @RequestParam @Valid @NotEmpty @NotBlank String filename,
            @Parameter(description = "New filename", required = true)
            @RequestBody @Valid @NotNull FileRequest fileRequest
    ) throws IOException {
        final var fileRenameRequest = new FileRenameRequest();
        fileRenameRequest.setNewFilename(fileRequest.getFilename());
        fileRenameRequest.setToUpdateFilename(filename);
        return fileServiceImpl.renameFile(fileRenameRequest);
    }

    public static String generateRandomLinkKey() {
        byte[] randomBytes = new byte[16];
        new Random().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
