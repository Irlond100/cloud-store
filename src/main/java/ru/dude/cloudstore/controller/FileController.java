package ru.dude.cloudstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dude.cloudstore.controller.docs.annotation.ApiErrorResponse;
import ru.dude.cloudstore.controller.docs.annotation.AuthTokenParameter;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.service.FileServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    @Operation(description = "Download file from cloud")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiErrorResponse
    @AuthTokenParameter
    @GetMapping("/file")
    public ResponseEntity<Resource> getFile(
            @Parameter(description = "Name of the file to generate one-time download link for")
            @RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        final var fileResource = fileServiceImpl.getFileResource(fileRequest);
        Resource resource = new ByteArrayResource(fileResource.getInputStream().readAllBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Operation(description = "Deletes a file")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiErrorResponse
    @AuthTokenParameter
    @DeleteMapping("/file")
    public void deleteFile(@RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        fileServiceImpl.deleteFile(fileRequest);
    }

    @Operation(description = "Uploads a file")
    @ApiResponse(responseCode = "200", description = "Success upload",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiErrorResponse
    @AuthTokenParameter
    @PostMapping(path = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void handleFileUpload(
            @Parameter(description = "Filename to rename", required = true)
            @RequestParam @Valid @NotEmpty @NotBlank String filename,
            FileUploadRequest fileUploadRequest) throws RuntimeException, IOException {
        fileServiceImpl.upload(fileUploadRequest, filename);
    }

    @Operation(description = "Get all files")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = APPLICATION_JSON_VALUE))
    @ApiErrorResponse
    @AuthTokenParameter
    @GetMapping("/list")
    public List<FileResponse> limitListUploaded(
            @RequestParam("limit") @Min(1) @Valid int limit) throws RuntimeException, IOException {
        return fileServiceImpl.getFileInfoList(limit);
    }

    @Operation(description = "Edit file name")
    @ApiResponse(responseCode = "200", description = "Success deleted",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))
    @ApiErrorResponse
    @AuthTokenParameter
    @PutMapping("/file")
    public void handleFileRename(
            @Parameter(description = "Filename to rename", required = true)
            @RequestParam @Valid @NotEmpty @NotBlank String filename,
            @Parameter(description = "New filename", required = true)
            @RequestBody @Valid @NotNull FileRequest fileRequest
    ) throws IOException {
        final var fileRenameRequest = new FileRenameRequest();
        fileRenameRequest.withNewFilename(fileRequest.name());
        fileRenameRequest.withToUpdateFilename(filename);
        fileServiceImpl.renameFile(fileRenameRequest);
    }

}
