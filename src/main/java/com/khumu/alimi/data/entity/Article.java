package com.khumu.alimi.data.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="article_article")
@Data
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn
    SimpleKhumuUser author;
}
