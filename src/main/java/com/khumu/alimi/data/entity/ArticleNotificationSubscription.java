package com.khumu.alimi.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_articlenotificationsubscription")
@Data
@Builder
public class ArticleNotificationSubscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long studyArticle;
    Long article;
    String subscriber;

    @Builder.Default
    Boolean isActivated = true;
}
