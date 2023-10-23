package ru.dude.cloudstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class FileResponse {
    private String filename;
    private Long size;
    private String date;
}
