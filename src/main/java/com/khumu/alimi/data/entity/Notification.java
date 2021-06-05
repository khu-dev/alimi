package com.khumu.alimi.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_notification")
@Data
@Builder
// DB에는 기본적으로 Column name이 Snake case.
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String kind;
    private String recipient;
    private String reference; // 참조 링크

//    @JsonProperty("is_read")// Jackson이 is라는 prefix를 삭제해버림.
    // boolean인 경우 발생하던 문젠데 Boolean으로 변경함으로써 해결
    @Builder.Default
    private Boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
