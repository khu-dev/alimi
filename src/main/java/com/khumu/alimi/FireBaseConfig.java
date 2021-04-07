package com.khumu.alimi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FireBaseConfig {
    @Value("${firebase.credential-file-path}")
    String FIREBASE_CREDENTIAL_FILE_PATH;

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
