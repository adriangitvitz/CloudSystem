package com.example.springapi.models.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Deletebucketresponse {
    private String bucket;
    private HttpStatus status;
}
