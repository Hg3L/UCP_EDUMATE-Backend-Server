package vn.base.edumate.common.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudVisionConfig {

    @Value("${system.gcloud-vision.credential.path}")
    private String credentialPath;

    @Bean
    public ImageAnnotatorClient imageAnnotatorClient() {
        try {
            InputStream inputStream = new ClassPathResource(credentialPath).getInputStream();
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
            return ImageAnnotatorClient.create(settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize ImageAnnotatorClient", e);
        }
    }
}

