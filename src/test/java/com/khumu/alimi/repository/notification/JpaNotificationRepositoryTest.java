package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
// test 용 db 설정. application.manifest꺼를 쓸 수도 있음.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaNotificationRepositoryTest {
    @Autowired
    private JpaNotificationRepositoryInterface jpaNotificationRepositoryIfc;
    private JpaNotificationRepository repo;

    @BeforeEach
    void setUp() {
        System.out.println("테스트 시작");
        repo = new JpaNotificationRepository(this.jpaNotificationRepositoryIfc);
    }
    @AfterEach
    void tearDown() {
        System.out.println("테스트 종료");
    }

    @Test
    public void save(){
        assertThat(repo).isNotNull();
        Notification original = new Notification(
                0L,
                "댓글이 생성되었습니다.",
                "이제~ 다시는~ 울지 않아~",
                new SimpleKhumuUser("jinsu"),
                false,
                null
        );
        Notification created = repo.create(original);
        assertThat(created.getId()).isNotEqualTo(0);
        assertThat(created.getId()).isNotNull();

        original = new Notification(
                null,
                "댓글이 생성되었습니다.",
                "이제~ 다시는~ 울지 않아~",
                new SimpleKhumuUser("jinsu"),
                false,
                null
        );
        assertThat(created.getId()).isNotEqualTo(0);
        assertThat(created.getId()).isNotNull();
    }

    @Test
    public void list(){
        SimpleKhumuUser recipient = new SimpleKhumuUser("jinsu");
        Notification original = new Notification(
                null,
                "댓글이 생성되었습니다.",
                "이제~ 다시는~ 울지 않아~",
                recipient,
                false,
                null
        );
        repo.create(original);

        assertThat(repo.list()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(repo.list(recipient.getUsername())).hasSizeGreaterThanOrEqualTo(1);
        assertThat(repo.list("wrongUsername")).hasSize(0);
    }

}