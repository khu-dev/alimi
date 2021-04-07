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
    @JoinColumn(name="board_id") // board_name이 아닌 board_id가 column name
    Board board;

    @ManyToOne
    @JoinColumn(name="author_id") // author_username이 아닌 author_id가 column name
    SimpleKhumuUser author;
}
