package com.RWI.Nidhi.user.imageUpload.configs3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.RWI.Nidhi.user.imageUpload.serviceS3.FileUpload;

@Configuration
public class AppConfig {
    @Bean
    public FileUpload fileUpload() {
        return new FileUpload();
    }

}
