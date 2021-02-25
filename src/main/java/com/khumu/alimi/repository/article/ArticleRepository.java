package com.khumu.alimi.repository.article;

import com.khumu.alimi.data.Article;
import com.khumu.alimi.data.Notification;

import java.util.List;

public interface ArticleRepository {
    Article get(Long id);
    List<Article> list();
    List<Article> list(String username);
}
