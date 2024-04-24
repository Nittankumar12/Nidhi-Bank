package com.aws.serviceS3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

//        String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;
//
////        Object downloadFile(String fileName) throws FileDownloadException, IOException;
//
//    String uploadFile(String bucketName, MultipartFile file) throws FileUploadException, IOException;

    String fileUplaod(String bucketName, MultipartFile file);

    boolean delete(String fileName);

    String createBucket(String bucketName);

    List<String> getBucketList();

    List<FileUpload> getBucketfiles(String bucketName);

    String softDeleteBucket(String bucketName);

    String hardDeleteBucket(String bucketName);

    String deleteFile(String bucketName, String fileName);

    FileUpload downloadFile(String bucketName, String fileName);


    }



