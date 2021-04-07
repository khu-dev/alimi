package com.khumu.alimi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

@SpringBootApplication
//@EnableJpaRepositories(repositoryImplementationPostfix = "JpaImpl")
public class AlimiApplication {
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${firebase.credential-file-path}")
	String FIREBASE_CREDENTIAL_FILE_PATH;

	public static void main(String[] args) {
		SpringApplication.run(AlimiApplication.class, args);
	}

	@Bean
	public Gson gson() {
		System.out.println("AlimiApplication.gson");
		return new Gson();
	}

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		System.out.println("AlimiApplication.firebaseApp");
		FileInputStream credentialFileInputStream = new FileInputStream(FIREBASE_CREDENTIAL_FILE_PATH);
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(credentialFileInputStream))
//			.setDatabaseUrl("")
				.build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		// 나중에 bean destroy 시에 app.delete를 해주는 게 좋긴 할 듯.
		// 한 App 내에 default app이
		return app;
	}
}



