package com.aws.exceptionS3;

public class FileUploadException extends SpringBootFileUploadException {
    public FileUploadException(String message) {
        super(message);
    }
}
