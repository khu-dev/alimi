package com.khumu.alimi.repository.article;

import com.khumu.alimi.data.Article;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class JpaArticleRepository implements ArticleRepository {

    private final JpaArticleRepositoryIfc jpa;

    @Override
    public Article get(Long id) {
        Optional<Article> article = jpa.findById(id);
        return article.get();
    }

    @Override
    public List<Article> list() {
        return jpa.findAll();
    }

    @Override
    public List<Article> list(String username) {
        return jpa.findByAuthorUsername(username);
    }
}
