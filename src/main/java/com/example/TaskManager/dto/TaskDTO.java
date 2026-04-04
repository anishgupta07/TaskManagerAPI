package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String status;
}
