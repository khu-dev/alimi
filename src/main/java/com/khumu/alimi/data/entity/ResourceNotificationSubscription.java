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
@Table
@Data
@Builder
public class ResourceNotificationSubscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 현재 구독 자체의 아이디
    Long id;
    // 구독하는 리소스의 아이디
    Long resourceId;
    @Enumerated(value=EnumType.STRING)
    ResourceKind resourceKind;
    String subscriber;

    @Builder.Default
    Boolean isActivated = true;
}
