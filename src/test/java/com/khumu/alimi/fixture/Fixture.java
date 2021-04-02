package com.khumu.alimi.fixture;

import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.entity.SimpleKhumuUser;

public class Fixture {
    public SimpleKhumuUser provideDefaultSimpleKhumuUser(String username) {
        return SimpleKhumuUser.builder().username(username).build();
    }

    public Comment provideDefaultComment(Long articleId, SimpleKhumuUser author) {
        return Comment.builder().articleId(articleId).authorObj(author).content("테스트용 댓글입니다.").build();
    }
}
