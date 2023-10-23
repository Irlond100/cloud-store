package ru.dude.cloudstore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FileUploadRequest {
    @NotEmpty
    String filename;
    @NotNull
    MultipartFile file;

//    public void setFile(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("File content must not be empty");
//        }
//        this.file = file;
//    }
}