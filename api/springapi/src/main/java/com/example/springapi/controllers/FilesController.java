package com.example.springapi.controllers;

import com.example.springapi.models.dto.Uploadfileresponse;
import com.example.springapi.models.entities.Uploadfiles;
import com.example.springapi.repositories.MinioRepo;
import com.example.springapi.repositories.PostgresRepo;
import com.example.springapi.services.FilesService;
import io.minio.errors.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/files")
public class FilesController implements FilesService {

    private final MinioRepo minioRepo;
    private final PostgresRepo postgresRepo;

    public FilesController(MinioRepo minioRepo, PostgresRepo postgresRepo) {
        this.minioRepo = minioRepo;
        this.postgresRepo = postgresRepo;
    }


    @Override
    public ResponseEntity<StreamingResponseBody> downloadfile(String bucket, String filename) {
        try {
            InputStream stream = minioRepo.downloadobject(bucket, filename);
            StreamingResponseBody responseBody = outputStream -> {
                int writebytes;
                byte[] data = new byte[1024];
                while ((writebytes = stream.read(data)) != -1) {
                    outputStream.write(data, 0, writebytes);
                }
                stream.close();
            };
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(responseBody);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error downloading file");
        }
    }

    @Override
    public void uploadfile(String bucket, MultipartFile file) {
        try {
            Uploadfileresponse uploaded = minioRepo.uploadtobucket(file.getInputStream(), bucket, file.getOriginalFilename(), file.getContentType(), file.getSize());
            if (!uploaded.isUploaded()) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bucket does not exists");
            }
            postgresRepo.save(new Uploadfiles(file.getOriginalFilename(), ZonedDateTime.now(), false, file.getSize(), bucket, uploaded.getUuid(), "logs"));
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error uploading file");
        }
    }
}
