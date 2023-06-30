package com.example.springapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Buckets {
    private String name;
    @JsonProperty("creationdate")
    private ZonedDateTime creationdate;
}