package ru.dude.cloudstore.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record FileRequest(
        @NotEmpty
        @NotBlank
        String name
) {

}