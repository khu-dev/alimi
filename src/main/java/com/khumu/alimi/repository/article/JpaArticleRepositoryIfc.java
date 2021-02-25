package com.khumu.alimi.repository.article;

import com.khumu.alimi.data.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaArticleRepositoryIfc extends JpaRepository<Article, Long> {
    @Query("select a from Article a where a.authorObj.username=:authorUsername")
    List<Article> findByAuthorUsername(@Param("authorUsername") String authorUsername);
}
