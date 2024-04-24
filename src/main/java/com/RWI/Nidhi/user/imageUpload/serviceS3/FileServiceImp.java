package com.aws.serviceS3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImp implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImp.class);

    @Autowired
    private AmazonS3 amazonS3;





    @Override
    public String fileUplaod(String bucketName, MultipartFile file) {
        String fileName = "";

        try {

            if (!amazonS3.doesBucketExistV2(bucketName)) {

                return "Bucket Not Exist";
            }

            fileName = UUID.randomUUID() + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            log.info("File Uploaded");

        } catch (SdkClientException | IOException e) {
            log.info("File Uploading exception");
            return "Exception";
        }
        return "File Uploaded Successfully \nFileName:- " + fileName;
    }


    @Override
    public boolean delete(String fileName) {
        return false;
    }

    @Override
    public String createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            // Because the CreateBucketRequest object doesn't specify a region, the
            // bucket is created in the region specified in the client.
            amazonS3.createBucket(new CreateBucketRequest(bucketName));
            // Verify that the bucket was created by retrieving it and checking its
            // location.
            return "Bucket Created \nBucket Name:-" + bucketName +"\nregion:-"
                    + amazonS3.getBucketLocation(new GetBucketLocationRequest(bucketName));
        }
        return "Bucket Already Exist";
    }

    @Override
    public List<String> getBucketList() {
        return amazonS3.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());

    }

    @Override
    public List<FileUpload> getBucketfiles(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No Bucket Found");
            return null;
        }
        return amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream()
                .map(file -> new FileUpload(file.getKey(), file.getSize(), file.getETag()))
                .collect(Collectors.toList());
    }

    @Override
    public String softDeleteBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No Bucket Found");
            return "No Bucket Found";
        }
        if (amazonS3.listObjectsV2(bucketName).isTruncated()) {
            amazonS3.deleteBucket(bucketName);
            return "Bucket Deleted Successfully";
        }
        return "Bucket have some files";    }

    @Override
    public String hardDeleteBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No Bucket Found");
            return "No Bucket Found";
        }
        ListObjectsV2Result results = amazonS3.listObjectsV2(bucketName);
        for (S3ObjectSummary s3ObjectSummary : results.getObjectSummaries()) {
            amazonS3.deleteObject(bucketName, s3ObjectSummary.getKey());
        }
        return "Bucket Deleted Successfully";
    }

    @Override
    public String deleteFile(String bucketName, String fileName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No Bucket Found");
            return "No Bucket Found";
        }
        amazonS3.deleteObject(bucketName, fileName);
        return "File Deleted Successfully";
    }

    @Override
    public FileUpload downloadFile(String bucketName, String fileName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No Bucket Found");
            return null;
        }
        S3Object s3object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUpload fileUpload = new FileUpload();
        try {
            fileUpload.setFile(FileCopyUtils.copyToByteArray(inputStream));
            fileUpload.setFileName(s3object.getKey());
            return fileUpload;
        } catch (Exception e) {
            return null;
        }
    }
}

