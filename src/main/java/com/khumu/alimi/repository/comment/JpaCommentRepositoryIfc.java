package com.khumu.alimi.repository.comment;

import com.khumu.alimi.data.Article;
import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCommentRepositoryIfc extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.articleObj.id=:articleId")
    List<Comment> findByArticleId(@Param("articleId") Long articleId);
}
