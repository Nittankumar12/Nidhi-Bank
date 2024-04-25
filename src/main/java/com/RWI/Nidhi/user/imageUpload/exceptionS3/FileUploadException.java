package com.RWI.Nidhi.user.imageUpload.exceptionS3;

public class FileUploadException extends SpringBootFileUploadException {
    public FileUploadException(String message) {
        super(message);
    }
}
