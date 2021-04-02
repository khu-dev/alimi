//package com.khumu.alimi.repository.comment;
//
//import com.khumu.alimi.data.entity.Comment;
//import com.khumu.alimi.repository.CommentRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//// test 용 db 설정. application.manifest꺼를 쓸 수도 있음.
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class CommentRepositoryTest {
//
//    @Autowired
//    CommentRepository jpaIfc;
//
//    CommentRepository repo;
//    List<Comment> fixtureComments;
//
//    // 실질적으로 BeforeEach랑 AfterEach에서 또 다시 Transactional을 정의할 필요는 없다.
//    // test method의 transaction을 따라감.
//    // ref: https://stackoverflow.com/questions/17308335/before-and-transactional
//    @BeforeEach
//    void setUp() {
//        repo = new CommentRepository(jpaIfc);
//        fixtureComments = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            fixtureComments.add(jpaIfc.save(new Comment("jinsu")));
//        }
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    @Test
//    public void list() {
//        List<Comment> l = repo.list();
//        assertThat(l).hasSizeGreaterThanOrEqualTo(1);
//    }
//
//    @Test
//    public void listFromArticle() {
//        // 만약 1번 게시물에 댓글이 없으면 fail...
//        assertThat(repo.listFromArticle(1L)).hasSizeGreaterThanOrEqualTo(3);
//        List<Comment> l = repo.listFromArticle(1L);
//        for (Comment c : l) {
//            assertThat(c.getArticleObj().getId()).isEqualTo(1L);
//        }
//    }
//}