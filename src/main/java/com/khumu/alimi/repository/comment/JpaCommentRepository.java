package com.khumu.alimi.repository.comment;

import com.khumu.alimi.data.Comment;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Primary
@AllArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    private final JpaCommentRepositoryIfc jpa;

    @Override
    public Comment create(Comment c) {
        return jpa.save(c);
    }

    @Override
    public List<Comment> list() {
        System.out.println("JpaCommentRepository.list");
        return jpa.findAll();
    }

    @Override
    public List<Comment> listFromArticle(Long articleId) {
        System.out.println("JpaCommentRepository.listFromArticle");
        return jpa.findByArticleId(articleId);
    }
}
