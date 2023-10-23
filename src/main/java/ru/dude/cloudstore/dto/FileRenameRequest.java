package ru.dude.cloudstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileRenameRequest {
    @NotEmpty
    private String newFilename;
    @NotEmpty
    private String toUpdateFilename;
}
