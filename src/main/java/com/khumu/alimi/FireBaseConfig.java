package com.khumu.alimi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FireBaseConfig {
    String FIREBASE_CREDENTIAL_FILE_PATH = "khumu-dev-firebase-credential.json";

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        System.out.println("AlimiApplication.firebaseApp");
        ClassPathResource classPathResource = new ClassPathResource(FIREBASE_CREDENTIAL_FILE_PATH);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(classPathResource.getInputStream()))
//			.setDatabaseUrl("")
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        // 나중에 bean destroy 시에 app.delete를 해주는 게 좋긴 할 듯.
        // 한 App 내에 default app이
        return app;
    }

    @Bean
    public FirebaseMessaging firebaseMessage(FirebaseApp app) {
        // app 이 생긴 뒤에 messaging instance를 얻을 수 있으므로
        // app을 인자로 받게 함.
        return FirebaseMessaging.getInstance();
    }
}
