package com.RWI.Nidhi.user.imageUpload.ControllerS3;

import com.aws.serviceS3.FileService;
import com.aws.serviceS3.FileUpload;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/s3")
public class S3FileController {
    @Autowired
    private FileService fileService;


    @PostMapping(value = "/bucket/create/{bucketName}")
    public String createBucket(@PathVariable String bucketName) {
        return fileService.createBucket(bucketName);
    }

    @GetMapping(value = "/bucket/list")
    public List<String> getBucketList() {
        return fileService.getBucketList();
    }

    @GetMapping(value = "/bucket/files/{bucketName}")
    public List<FileUpload> getBucketfiles(@PathVariable String bucketName) {
        return fileService.getBucketfiles(bucketName);
    }

    @DeleteMapping(value = "/bucket/delete/hard/{bucketName}")
    public String hardDeleteBucket(@PathVariable String bucketName) {
        return fileService.hardDeleteBucket(bucketName);
    }

    @DeleteMapping(value = "/bucket/delete/{bucketName}")
    public String softDeleteBucket(@PathVariable String bucketName) {
        return fileService.softDeleteBucket(bucketName);
    }

//    @PostMapping(value = "/file/upload/{bucketName}")
//    public String fileUplaod(@PathVariable String bucketName, MultipartFile file) throws IOException {
//        System.out.println(file.getOriginalFilename() + " is the file name");
//        System.out.println();
//        return "running";
////        return fileService.fileUplaod(bucketName, file);
//    }

    @PostMapping(value = "/file/upload/{bucketName}")
    public String fileUplaod(@PathVariable String bucketName ,@RequestParam ("bucketName")  MultipartFile file) {
        System.out.println("file name is: " + file.getName() + " get location: " + file.getResource());
        return fileService.fileUplaod(bucketName, file);
    }

    @DeleteMapping(value = "/file/delete/{bucketName}/{fileName}")
    public String deleteFile(@PathVariable String bucketName, @PathVariable String fileName) {
        return fileService.deleteFile(bucketName, fileName);
    }

    @GetMapping(value = "/file/download/{bucketName}/{fileName}")
    public StreamingResponseBody downloadFile(@PathVariable String bucketName, @PathVariable String fileName,
                                              HttpServletResponse httpResponse) {
        FileUpload downloadFile = fileService.downloadFile(bucketName, fileName);
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s\"", downloadFile.getFileName()));
        return new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                outputStream.write(downloadFile.getFile());
                outputStream.flush();
            }
        };
    }

}
