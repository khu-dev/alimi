package com.khumu.alimi.controller.notification;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 실패하는 테스트임.
 * MockMvc가 SpyBean을 이용하지 않고 SpringBoot config를 이용함....
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
//public class NotificationControllerAutoConfigureMvcTest {
//    // 의존성이 안바뀌어~!~!
//    // memory를 써야하는데 jpa를 써...
//    // memory를 일종의 mocking(spy)처럼 이용하도록
//    @SpyBean
//    MemoryNotificationRepository memoryNotificationRepository;
//    @SpyBean
//    NotificationServiceImpl notificationService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void listNotifications() throws Exception {
//
//        this.mockMvc.perform(get("/api/notifications"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("data").isArray())
//                // setUp에서 만드는 data에 의해
//                .andExpect(jsonPath("data.length()").value(greaterThanOrEqualTo(1)))
//                .andExpect(jsonPath("data.length()").value(lessThanOrEqualTo(10)));
//
//    }
//    @BeforeEach
//    void setUp() {
//        Notification n1 = new Notification(
//                null,
//                "댓글이 생성되었습니다.",
//                "hello, world 댓글이랍니다~!",
//                "커뮤니티",
//                SimpleKhumuUserDto.builder().username("jinsu").build(),
//                false,
//                null
//        );
//        memoryNotificationRepository.create(n1);
//
//        Notification n2 = new Notification(
//                null,
//                "광고",
//                "뭐?! 쿠뮤에서 이번에 새 팀원을 모집한다구~?!",
//                "ad",
//                SimpleKhumuUserDto.builder().username("jinsu").build(),
//                false,
//                null
//        );
//        memoryNotificationRepository.create(n1);
//    }
//
//    @AfterEach
//    void tearDown() {
//        memoryNotificationRepository.clear();
//    }
//
//}