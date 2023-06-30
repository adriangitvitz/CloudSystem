package com.example.springapi.models.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class Listobjectsresponse {
    private String name;
    private boolean isdir;
    private ZonedDateTime lastmodified;
    private Map<String, String> metadata;
}
