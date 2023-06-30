package com.example.springapi.controllers;

import com.example.springapi.models.Bucket;
import com.example.springapi.models.Buckets;
import com.example.springapi.models.dto.Createbucketresponse;
import com.example.springapi.models.dto.Deletebucketresponse;
import com.example.springapi.models.dto.Listobjectsresponse;
import com.example.springapi.repositories.MinioRepo;
import com.example.springapi.services.BucketService;
import io.minio.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/buckets")
public class BucketController implements BucketService {
    private final MinioRepo minioRepo;

    public BucketController(MinioRepo minioRepo) {
        this.minioRepo = minioRepo;
    }

    @Override
    public ResponseEntity<List<Buckets>> getBuckets() {
        try {
            List<Buckets> buckets = minioRepo.getBuckets();
            return new ResponseEntity<>(buckets, HttpStatus.OK);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error getting buckets");
        }
    }

    @Override
    public ResponseEntity<Createbucketresponse> createBucket(Bucket bucket) {
        try {
            Createbucketresponse createbucketresponse = minioRepo.createBucket(bucket.getName());
            return new ResponseEntity<>(createbucketresponse, createbucketresponse.getStatus());
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating bucket");
        }
    }

    @Override
    public ResponseEntity<Deletebucketresponse> deleteBucket(String bucket) {
        try{
           Deletebucketresponse deletebucketresponse =  minioRepo.removeBucket(bucket);
           return new ResponseEntity<>(deletebucketresponse, deletebucketresponse.getStatus());
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting bucket");
        }
    }

    @Override
    public ResponseEntity<List<Listobjectsresponse>> listObjects(String bucket) {
        try{
            List<Listobjectsresponse> listobjectsresponses = minioRepo.listBucketObjects(bucket);
            return new ResponseEntity<>(listobjectsresponses, HttpStatus.OK);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error listing objects");
        }
    }
}
