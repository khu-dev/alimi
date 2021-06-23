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
public class PushOption {
    @Id
    @Column
    // username
    String id;
    @Builder.Default
    Boolean isCommentNotificationActivated = true;

    @Builder.Default
    Boolean isAnnouncementNotificationActivated=true;

    @Builder.Default
    Boolean isKhumuNotificationActivated=true;
}
