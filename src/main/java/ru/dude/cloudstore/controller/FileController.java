package ru.dude.cloudstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dude.cloudstore.controller.common.*;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.service.FileServiceImpl;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileServiceImpl fileServiceImpl;
    private final Map<String, Resource> oneTimeLinks = new HashMap<>();

    @Operation(description = "Download file from cloud")
    @CommonApiResponsesDownloadFile
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
    @CommonApiResponsesDelete
    @AuthTokenParameter
    @DeleteMapping("/file")
    public void deleteFile(@RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        fileServiceImpl.deleteFile(fileRequest);
    }

    @Operation(description = "Uploads a file")
    @CommonApiResponsesFile
    @AuthTokenParameter
    @PostMapping(path = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void handleFileUpload(
            @Parameter(description = "Filename to rename", required = true)
            @RequestParam @Valid @NotEmpty @NotBlank String filename,
            FileUploadRequest fileUploadRequest) throws RuntimeException, IOException {
        fileServiceImpl.upload(fileUploadRequest, filename);
    }

    @Operation(description = "Get all files")
    @CommonApiResponsesList
    @AuthTokenParameter
    @GetMapping("/list")
    public List<FileResponse> limitListUploaded(
            @RequestParam("limit") @Min(1) @Valid int limit) throws RuntimeException, IOException {
        return fileServiceImpl.getFileInfoList(limit);
    }

    @Operation(description = "Edit file name")
    @CommonApiResponsesFileRename
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

    public static String generateRandomLinkKey() {
        byte[] randomBytes = new byte[16];
        new Random().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
