package com.RWI.Nidhi.user.imageUpload.exceptionS3;

public class FileEmptyException extends SpringBootFileUploadException{
    public FileEmptyException(String message) {
        super(message);
    }
}
