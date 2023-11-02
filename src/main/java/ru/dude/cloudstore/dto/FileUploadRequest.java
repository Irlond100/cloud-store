package ru.dude.cloudstore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        @NotEmpty
        String hash,
        @NotNull
        MultipartFile file
) {
}