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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.service.FileServiceImpl;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileServiceImpl fileServiceImpl;


    @AuthTokenParameter
    @GetMapping("/files")
    public List<String> getFiles() throws IOException {
        Path folder = Paths.get("src/main/resources/files");
        ArrayList<String> files = new ArrayList<>();
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            Files.walkFileTree(folder, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    files.add(file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return files;
    }

    @Operation(description = "Deletes a file")
    @ApiResponse(responseCode = "200", description = "'Delete operation' success message",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))

    @AuthTokenParameter
    @DeleteMapping("/file")
    public String deleteFile(@RequestParam @Valid @NotEmpty @NotBlank String filename) throws IOException {
        final var fileRequest = new FileRequest(filename);
        return fileServiceImpl.deleteFile(fileRequest);
    }

    @Operation(description = "Uploads a file")
    @ApiResponse(responseCode = "200", description = "Upload operation success message",
            content = @Content(mediaType = TEXT_PLAIN_VALUE))

    @AuthTokenParameter
    @PostMapping("file")
    public String handleFileUpload(
            @ModelAttribute @Valid FileUploadRequest fileUploadRequest) throws RuntimeException, IOException {
        return fileServiceImpl.upload(fileUploadRequest);
    }

    @Operation(description = "Returns list of uploaded files up to specified limit")
    @ApiResponse(responseCode = "200", description = "List of file-info",
            content = @Content(mediaType = "application/json"))

    @AuthTokenParameter
    @GetMapping("list")
    public List<FileResponse> limitListUploaded(
            @RequestParam("limit") @Min(1) @Valid int limit) throws RuntimeException, IOException {
        return fileServiceImpl.getFileInfoList(limit);
    }
}
