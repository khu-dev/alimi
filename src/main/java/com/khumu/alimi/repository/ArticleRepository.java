package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select a from Article a where a.author.username = :authorUsername")
    List<Article> findByAuthorUsername(String authorUsername);
}
