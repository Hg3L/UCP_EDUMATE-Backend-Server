package vn.base.edumate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class EdumateBackendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdumateBackendServerApplication.class, args);
    }
}
