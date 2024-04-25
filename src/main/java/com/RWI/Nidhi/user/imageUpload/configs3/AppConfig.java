package com.RWI.Nidhi.user.imageUpload.configs3;

import com.aws.serviceS3.FileUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FileUpload fileUpload() {
        return new FileUpload();
    }

}
