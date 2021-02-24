package com.khumu.alimi.repository.article;

import com.khumu.alimi.data.Notification;

import java.util.List;

public interface ArticleRepository {
    List<Notification> list();
    List<Notification> list(String username);
}
