package com.example.springapi.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface FilesService {
    @GetMapping("{bucket}/{filename}")
    ResponseEntity<StreamingResponseBody> downloadfile(@PathVariable String bucket, @PathVariable String filename);
    @PutMapping("{bucket}")
    @ResponseStatus(HttpStatus.OK)
    void uploadfile(@PathVariable String bucket, @RequestParam("file") MultipartFile file);
}