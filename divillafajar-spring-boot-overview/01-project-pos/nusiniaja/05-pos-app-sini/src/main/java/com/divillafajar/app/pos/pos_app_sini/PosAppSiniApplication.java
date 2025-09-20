package com.divillafajar.app.pos.pos_app_sini;

import com.divillafajar.app.pos.pos_app_sini.service.fiture.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PosAppSiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosAppSiniApplication.class, args);
	}

}

@Component
@RequiredArgsConstructor
class DataInitializer implements CommandLineRunner {
    private final FeatureService featureService;

    @Override
    public void run(String... args) {
        featureService.ensureBasicFeature();
        System.out.println("✅ Feature Basic ensured after schema ready");
    }
}
