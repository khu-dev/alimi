package com.khumu.alimi.data.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="comment_comment")
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String kind;
    String state;
    String content;

    @ManyToOne
    @JoinColumn(name="author_id")
    SimpleKhumuUser author;

    @ManyToOne
    @JoinColumn
    Article article;

    @ManyToOne
    @JoinColumn
    Comment parent;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
