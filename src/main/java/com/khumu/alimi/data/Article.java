package com.khumu.alimi.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.ConstructorParameters;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="article_article")
public class Article {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "username")
    @SerializedName("author")
    SimpleKhumuUser authorObj;

    String author;

    public Article(Long id) {
        this.id = id;
        this.authorObj = new SimpleKhumuUser("jinsu");
        this.author = "jinsu";
    }
    public Article(Long id, String authorUsername) {
        this.id = id;
        this.authorObj = new SimpleKhumuUser(authorUsername);
        this.author = authorUsername;
    }
}
