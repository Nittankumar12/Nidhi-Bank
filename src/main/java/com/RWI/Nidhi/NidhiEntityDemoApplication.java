package com.RWI.Nidhi;

import com.RWI.Nidhi.configuration.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NidhiEntityDemoApplication {

    @Autowired
    private TwilioConfig twilioConfig;


    public static void main(String[] args) {
        SpringApplication.run(NidhiEntityDemoApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173/")
//                        .allowedMethods("GET","PUT","POST","DELETE","OPTIONS")
//                        .allowCredentials(true)
//                        .allowedHeaders("Content-Type"
//                                ,"x-xsrf-token"
//                                ,"Authorization"
//                                ,"Access-Control-Allow-Headers"
//                                ,"Access-Control-Request-Method"
//                                ,"Access-Control-Request-Headers"
//                                ,"Origin","Accept","X-Requested-With")
//                        .maxAge(3600);
//            }
//        };
//    }
}
