package com.khumu.alimi.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
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

    @ManyToOne
    @JoinColumn(name="recipient_id")
    @JsonIgnore
    private SimpleKhumuUser recipient;

    @JsonProperty("is_read")// Jackson이 is를 삭제해버리는데 왜지..
    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
