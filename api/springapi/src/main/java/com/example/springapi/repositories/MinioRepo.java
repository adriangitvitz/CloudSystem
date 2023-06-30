package com.example.springapi.repositories;

import com.example.springapi.configuration.Minioconfig;
import com.example.springapi.models.Buckets;
import com.example.springapi.models.dto.Createbucketresponse;
import com.example.springapi.models.dto.Deletebucketresponse;
import com.example.springapi.models.dto.Listobjectsresponse;
import com.example.springapi.models.dto.Uploadfileresponse;
import com.example.springapi.util.Itemparser;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class MinioRepo {

    private final Minioconfig minioconfig;

    public MinioRepo(Minioconfig minioconfig) {
        this.minioconfig = minioconfig;
    }

    public MinioClient connect() {
        return MinioClient.builder()
                .endpoint(minioconfig.getEndpoint())
                .credentials(minioconfig.getAccesskey(), minioconfig.getSecretkey())
                .build();
    }

    public Createbucketresponse createBucket(String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Createbucketresponse createbucketresponse = new Createbucketresponse();
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucket).build();
        MinioClient client = connect();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucket).build();
        if (!client.bucketExists(bucketExistsArgs)) {
            client.makeBucket(makeBucketArgs);
            createbucketresponse.setName(bucket);
            createbucketresponse.setStatus(HttpStatus.CREATED);
        } else {
            createbucketresponse.setName(bucket);
            createbucketresponse.setStatus(HttpStatus.CONFLICT);
        }
        return createbucketresponse;
    }

    public List<Buckets> getBuckets() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Buckets> bucketsList = new ArrayList<>();
        List<Bucket> buckets = connect().listBuckets();
        if (buckets.size() > 0) {
            bucketsList = buckets.stream().map(x -> {
                Buckets bucket = new Buckets();
                bucket.setName(x.name());
                bucket.setCreationdate(x.creationDate());
                return bucket;
            }).toList();
        }
        return bucketsList;
    }

    public Deletebucketresponse removeBucket(String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder().bucket(bucket).build();
        Deletebucketresponse deletebucketresponse = new Deletebucketresponse();
        MinioClient client = connect();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucket).build();
        if (client.bucketExists(bucketExistsArgs)) {
            connect().removeBucket(removeBucketArgs);
            deletebucketresponse.setBucket(bucket);
            deletebucketresponse.setStatus(HttpStatus.ACCEPTED);
        } else {
            deletebucketresponse.setBucket(bucket);
            deletebucketresponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return deletebucketresponse;
    }

    public List<Listobjectsresponse> listBucketObjects(String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient client = connect();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucket).build();
        List<Listobjectsresponse> listobjectsresponses = new ArrayList<>();
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucket)
                .recursive(true)
                .build();
        if (client.bucketExists(bucketExistsArgs)) {
            Iterable<Result<Item>> object_results = client.listObjects(listObjectsArgs);
            listobjectsresponses = StreamSupport.stream(object_results.spliterator(), false)
                    .map(itemResult -> {
                        try {
                            return itemResult.get();
                        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                                 InvalidKeyException | InvalidResponseException | IOException |
                                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(Itemparser::parsebucketobjectitems)
                    .toList();
        }
        return listobjectsresponses;
    }

    public Uploadfileresponse uploadtobucket(InputStream inputStream, String bucket, String filename, String contenttype, long size) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient client = connect();
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucket).build();
        Uploadfileresponse uploadfileresponse = new Uploadfileresponse();
        UUID uuid = UUID.randomUUID();
        if (client.bucketExists(bucketExistsArgs)) {
            Map<String, String> metadata = new HashMap<>();
            String uuid_upload = uuid.toString();
            metadata.put("X-Amz-Meta-Uuid",uuid_upload);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .userMetadata(metadata)
                    .stream(inputStream, -1, size)
                    .contentType(contenttype)
                    .build();
            client.putObject(putObjectArgs);
            inputStream.close();
            uploadfileresponse.setUuid(uuid_upload);
            uploadfileresponse.setUploaded(true);
            return uploadfileresponse;
        }
        uploadfileresponse.setUuid("");
        uploadfileresponse.setUploaded(false);
        return uploadfileresponse;
    }

    public InputStream downloadobject(String bucket, String filename) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient client = connect();
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(filename).build();
        return client.getObject(getObjectArgs);
    }
}
