package com.fundsphere.erp.config;

import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    // =====================================================
    // FIREBASE INITIALIZATION
    // =====================================================

    @PostConstruct
    public void initializeFirebase() {

        try {

            // =================================================
            // CHECK IF FIREBASE IS ALREADY INITIALIZED
            // =================================================

            if (!FirebaseApp.getApps().isEmpty()) {

                System.out.println(
                        "Firebase is already initialized."
                );

                return;
            }


            // =================================================
            // LOAD FIREBASE SERVICE ACCOUNT JSON
            // =================================================

            InputStream serviceAccount =
                    getClass()
                            .getClassLoader()
                            .getResourceAsStream(
                                "firebase/fundsphere-erp-firebase-adminsdk-fbsvc-4df97033a4.json"
                            );


            if (serviceAccount == null) {

                throw new IllegalStateException(
                        "Firebase Service Account JSON file not found."
                );
            }


            // =================================================
            // FIREBASE OPTIONS
            // =================================================

            FirebaseOptions options =
                    FirebaseOptions.builder()

                            .setCredentials(
                                    GoogleCredentials
                                            .fromStream(
                                                    serviceAccount
                                            )
                            )

                            .build();


            // =================================================
            // INITIALIZE FIREBASE
            // =================================================

            FirebaseApp.initializeApp(
                    options
            );


            System.out.println(
                    "================================================="
            );

            System.out.println(
                    "Firebase connected successfully."
            );

            System.out.println(
                    "FundSphere ERP Firebase initialization completed."
            );

            System.out.println(
                    "================================================="
            );


        } catch (Exception e) {

            System.err.println(
                    "================================================="
            );

            System.err.println(
                    "Firebase initialization failed."
            );

            System.err.println(
                    e.getMessage()
            );

            System.err.println(
                    "================================================="
            );

            throw new RuntimeException(
                    "Unable to initialize Firebase.",
                    e
            );
        }
    }
}