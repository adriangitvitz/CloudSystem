package com.example.springapi.models.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Createbucketresponse {
    private String name;
    private HttpStatus status;
}
