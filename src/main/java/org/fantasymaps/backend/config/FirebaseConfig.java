package org.fantasymaps.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Bean
    public Bucket initializeFirebase() throws IOException {
        ClassPathResource resource = new ClassPathResource("firebaseServiceAccountKey.json");
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("fantasy-maps-web.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        return StorageClient.getInstance().bucket();
    }
}
