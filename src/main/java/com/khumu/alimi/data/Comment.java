package com.khumu.alimi.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="comment_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String kind;
    String state;
    String content;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "username")
    @SerializedName("author")
    SimpleKhumuUser authorObj;

    @ManyToOne
    @JoinColumn(name="article_id", referencedColumnName = "id")
    @SerializedName("article")
    @JsonIgnore
    Article articleObj;



    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonIgnore
    Comment parentObj;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonInclude
    @Transient
    String author;
    @JsonInclude
    @Transient
    Long article;
    @JsonInclude
    @Transient
    int parent;

    /**
     * Article ID가 1인 Comment 생성
     * @param author
     */
    public Comment(String author) {
        this.author = author;
        this.authorObj = new SimpleKhumuUser(author);
        this.content = "Fixture 댓글입니다" + System.currentTimeMillis();
        this.articleObj = new Article(1L);
        this.article = 1L;

        this.kind = "named";
        this.state = "exists";
        this.parentObj = null;
    }

    public Comment(String author, Long article) {
        this.author = author;
        this.authorObj = new SimpleKhumuUser(author);
        this.content = "Fixture 댓글입니다" + System.currentTimeMillis();
        this.articleObj = new Article(article);
        this.article = 1L;

        this.kind = "named";
        this.state = "exists";
        this.parentObj = null;
    }
}
