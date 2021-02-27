package com.khumu.alimi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // application
@TestPropertySource(locations= {"classpath:application.properties", "classpath:application-cred.properties"})
public class FireBaseAdminTest {
    @Value("${firebase.credential.path}")
    String credentialPath;
    @Value("${firebase.credential.database-url}")
    String firebaseDBUrl;
    @Value("${firebase.credential.default-device-token}")
    String defaultDeviceToken;

    @Test
    public void 올바른_values(){
        System.out.println(credentialPath);
        assertThat(credentialPath).isNotEmpty();
        assertThat(credentialPath).isNotNull();
    }
    @Test
    public void 올바른_FireBaseAdminCredentialFile() {
        System.out.println(credentialPath);
        InputStream wrongStream = FireBaseAdminTest.class.getClassLoader().getResourceAsStream("/khumu-dev-firebase-credential-wrong-name.json");
        assertThat(wrongStream).isNull();
        // /com/main/resources/khumu-dev-firebase-credential-wrong-name.json
        // 의 file에 대한 input stream

        assertDoesNotThrow(()->{
            InputStream credential = FireBaseAdminTest.class.getClassLoader().getResourceAsStream(credentialPath);
            assertThat(credential).isNotNull();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credential))
                    .setDatabaseUrl(firebaseDBUrl)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            assertThat(app).isNotNull();
            app.delete(); // 같은 이름의 app은 singleton으로 유지되어야하는 듯. 따라서 delete 해줘야 함.
        });
    }
}
