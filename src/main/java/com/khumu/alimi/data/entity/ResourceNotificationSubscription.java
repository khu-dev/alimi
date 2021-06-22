package com.khumu.alimi.data.entity;

import com.khumu.alimi.data.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_resourcenotificationsubscription")
@Data
@Builder
public class ResourceNotificationSubscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long studyArticle;
    Long article;
    Long announcement;
    @Enumerated(value=EnumType.STRING)
    ResourceKind resourceKind;
    String subscriber;

    @Builder.Default
    Boolean isActivated = true;
}
