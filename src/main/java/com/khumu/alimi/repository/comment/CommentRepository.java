package com.khumu.alimi.repository.comment;

import com.khumu.alimi.data.Comment;

import java.util.List;

public interface CommentRepository {
    Comment create(Comment c);
    List<Comment> list();
    List<Comment> listFromArticle(Long articleId);
}
