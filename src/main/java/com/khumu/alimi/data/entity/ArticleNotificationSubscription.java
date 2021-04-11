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
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;

    @OneToOne
    @JoinColumn(name = "subscriber_id")
    SimpleKhumuUser subscriber;

    @Builder.Default
    Boolean isActivated = true;
}
