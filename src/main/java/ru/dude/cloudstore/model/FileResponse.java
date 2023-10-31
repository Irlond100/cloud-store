package ru.dude.cloudstore.model;

public record FileResponse(
        String filename,
        Long size,
        String date
) {

}
