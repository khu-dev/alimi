package com.khumu.alimi;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.io.InputStream;

import static com.auth0.jwt.JWT.require;

@SpringBootApplication
//@EnableJpaRepositories(repositoryImplementationPostfix = "JpaImpl")
public class AlimiApplication {
	@Value("${jwt.secret}")
	private String jwtSecret;

	public static void main(String[] args) {
		SpringApplication.run(AlimiApplication.class, args);
	}

	@Bean
	public Gson gson(){
		System.out.println("AlimiApplication.gson");
		return new Gson();
	}

	@Bean
	public FirebaseApp firebaseApp() throws IOException{
		System.out.println("AlimiApplication.firebaseApp");
		InputStream credential = AlimiApplication.class.getResourceAsStream("/khumu-dev-firebase-credential.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(credential))
			.setDatabaseUrl("")
			.build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		// 나중에 bean destroy 시에 app.delete를 해주는 게 좋긴 할 듯.
		// 한 App 내에 default app이 
		return app;
		// token
		//
//		Message message = Message.builder()
//				.setToken(testDeviceToken)
//				.setAndroidConfig(AndroidConfig.builder()
//						.setNotification(
//								AndroidNotification.builder()
//										.setTitle("TDD를 수행 중입니다.")
//										.setBody("What do you think the advantages of TDD is?")
//										.build()).setTtl(500)
//						.setTtl(500)
//						.build())
//				.build();
//		String resp = FirebaseMessaging.getInstance().send(message);
//		System.out.println(resp);
	}

	@Bean
	public JWTVerifier jwtVerifier(){
		JWTVerifier jwtVerifier = require(Algorithm.HMAC256(jwtSecret)).build();
		return jwtVerifier;
	}
}
