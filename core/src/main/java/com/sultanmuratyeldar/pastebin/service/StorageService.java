package com.sultanmuratyeldar.pastebin.service;

import com.sultanmuratyeldar.pastebin.exception.StorageException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${MINIO_BUCKET}")
    private String bucket;

    // Ensure bucket exists
    private void ensureBucket() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            // If we cannot even check/create the bucket, it is a critical error
            throw new StorageException("Error initializing storage (S3 Bucket)", e);
        }
    }

    // Upload input stream
    public void upload(String objectName, InputStream data, long size, String contentType) {
        ensureBucket();
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(data, size, -1)
                    .contentType(contentType == null ? "application/octet-stream" : contentType)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            throw new StorageException("Failed to upload file: " + objectName, e);
        }
    }

    // Download as InputStream
    public byte[] download(String objectName) {
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        )) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new StorageException("Failed to download file: " + objectName, e);
        }
    }

    // Check exists
    public boolean exists(String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
            return true;
        } catch (ErrorResponseException e) {
            // Error code "NoSuchKey" means the file does not exist. This is a normal situation, return false
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            // If it is another error (e.g., access denied), log it, but for exists() it is often safer to return false
            log.warn("Error checking existence of file {}: {}", objectName, e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Unexpected error checking file {}: {}", objectName, e.getMessage());
            return false;
        }
    }

    public void delete(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting: "+ fileName, e);
        }
    }

    public void uploadString(String objectName, String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        upload(objectName, new ByteArrayInputStream(bytes), bytes.length, "text/plain; charset=utf-8");
    }
}