package com.example.notification_service;

import com.example.notificationservice.NotificationServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = NotificationServiceApplication.class)
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

        @Test
        void contextLoads() {
        }

}
