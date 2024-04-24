package com.aws.exceptionS3;

public class FileEmptyException extends SpringBootFileUploadException{
    public FileEmptyException(String message) {
        super(message);
    }
}
