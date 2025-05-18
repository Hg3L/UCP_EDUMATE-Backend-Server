package vn.base.edumate.common.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${system.gcloud-project.id}")
    private String projectId;

    @Value("${system.gcloud-project.region}")
    private String region;

    @Value("${system.gcloud-project.model}")
    private String modelName;

    @Bean
    public VertexAI vertexAI() {
        return new VertexAI(projectId, region);
    }

    @Bean
    public GenerativeModel generativeModel(VertexAI vertexAI) {
        return new GenerativeModel(modelName, vertexAI);
    }

}
