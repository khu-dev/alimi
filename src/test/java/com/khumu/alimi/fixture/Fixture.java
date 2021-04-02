package com.khumu.alimi.fixture;

import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.Board;
import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.entity.SimpleKhumuUser;

public class Fixture {
    public static Board provideDefaultBoard() {
        return Board.builder().category("커뮤니티").name("test_board").displayName("테스트용 게시판").build();
    }
    public static SimpleKhumuUser provideDefaultSimpleKhumuUser(String username) {
        return SimpleKhumuUser.builder().username(username).password("123123").build();
    }

    public static Comment provideDefaultComment(Article article, SimpleKhumuUser author) {
        return Comment.builder().article(article).author(author).content("테스트용 댓글입니다.").build();
    }

    public static Article provideDefaultArticle(Board board, SimpleKhumuUser author) {
        return Article.builder().board(board).author(author).build();
    }
}
