package vn.base.edumate.common.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Value("${system.cloudinary.cloud_name}")
    private String cloudName;

    @Value("${system.cloudinary.api_key}")
    private String apiKey;

    @Value("${system.cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }
}
