package com.khumu.alimi.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_notification")
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
    private SimpleKhumuUser recipient;
//    private String recipientId;
    @JsonProperty("is_read")// Jackson이 is를 삭제해버리는데 왜지..
    private boolean isRead;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Notification(String recipientUsername, String content) {
        this.recipient = new SimpleKhumuUser(recipientUsername);
        this.title = "임시 제목";
        this.content = content;
        this.kind = "mock";
        this.isRead = false;
    }
}
