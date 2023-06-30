package com.example.springapi.services;

import com.example.springapi.models.Bucket;
import com.example.springapi.models.Buckets;
import com.example.springapi.models.dto.Createbucketresponse;
import com.example.springapi.models.dto.Deletebucketresponse;
import com.example.springapi.models.dto.Listobjectsresponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BucketService {
    @GetMapping("")
    ResponseEntity<List<Buckets>> getBuckets();
    @PostMapping("")
    ResponseEntity<Createbucketresponse> createBucket(@Valid @RequestBody Bucket bucket);
    @DeleteMapping("{bucket}")
    ResponseEntity<Deletebucketresponse> deleteBucket(@PathVariable String bucket);
    @GetMapping("{bucket}")
    ResponseEntity<List<Listobjectsresponse>> listObjects(@PathVariable  String bucket);
}