package ru.dude.cloudstore.dto;

import jakarta.validation.constraints.NotEmpty;

public record FileRenameRequest(
        @NotEmpty
        String newFilename,
        @NotEmpty
        String toUpdateFilename
) {

    public FileRenameRequest() {
        this("", "");
    }

    public FileRenameRequest withNewFilename(String newFilename) {
        return new FileRenameRequest(newFilename, this.toUpdateFilename);
    }

    public FileRenameRequest withToUpdateFilename(String toUpdateFilename) {
        return new FileRenameRequest(this.newFilename, toUpdateFilename);
    }

}
