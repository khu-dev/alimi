package com.khumu.alimi.repository.comment;

import com.khumu.alimi.data.Article;
import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.Notification;
import lombok.AllArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
@Primary
@AllArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    private final JpaCommentRepositoryIfc jpa;

    @Override
    public Comment create(Comment comment) {
        Comment c = jpa.save(comment);
        preprocess(c);
        return c;
    }

    @Override
    public List<Comment> list() {
        List<Comment> comments = jpa.findAll();
        for (Comment c : comments) {
            preprocess(c);
        }
        return comments;
    }

    @Override
    public List<Comment> listFromArticle(Long articleId) {
        System.out.println("JpaCommentRepository.listFromArticle");
        List<Comment> comments = jpa.findByArticleId(articleId);
        for (Comment c : comments) {
            preprocess(c);
        }
        return comments;
    }

    public void preprocess(Comment comment) {
        if (comment.getArticleObj() == null) {
            comment.setArticleObj(new Article(comment.getArticleId()));
        } else {
            comment.setArticleId(comment.getArticleObj().getId());
        }
    }
}
