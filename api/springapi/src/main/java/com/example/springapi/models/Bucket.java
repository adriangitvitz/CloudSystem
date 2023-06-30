package com.example.springapi.models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Bucket {
    // TODO: Add field validations for special characters
    @NotNull(message = "Name is required")
    private String name;
}