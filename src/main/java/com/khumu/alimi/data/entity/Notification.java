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
@Table
@Data
@Builder
// DB에는 기본적으로 Column name이 Snake case.
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String title;
    String content;
    String kind;
    String recipient;
    // e.g. articles/1, articles?board=hot
    String reference; // 참조 링크
    // 누르면 바로 이동할 URL
    // e.g. https://www.naver.com/
    String link;

//    @JsonProperty("is_read")// Jackson이 is라는 prefix를 삭제해버림.
    // boolean인 경우 발생하던 문젠데 Boolean으로 변경함으로써 해결
    @Builder.Default
    Boolean isRead = false;

    @CreationTimestamp
    LocalDateTime createdAt;
}
