package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * Django가 이용하는 Comment DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDto {
    Long id;
    String title;
    String content;
    String kind;
    String recipient;
    String reference; // 참조 링크

    // @JsonProperty("is_read")// Jackson이 is라는 prefix를 삭제해버림.
    // boolean인 경우 발생하던 문젠데 Boolean으로 변경함으로써 해결
    @Builder.Default
    Boolean isRead = false;
    LocalDateTime createdAt;
}