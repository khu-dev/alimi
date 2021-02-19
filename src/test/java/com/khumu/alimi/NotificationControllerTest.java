package com.khumu.alimi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest(classes={NotificationController.class, NotificationServiceImpl.class, MemoryNotificationRepository.class}, properties = "classpath:application.properties")
@SpringBootTest(classes=AlimiApplication.class)
//@AutoConfigureMockMvc
public class NotificationControllerTest {

//    @Autowired
//    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
//        this.mockMvc.perform(get("/"))
//                .andExpect(status().isNotFound());
    }
}