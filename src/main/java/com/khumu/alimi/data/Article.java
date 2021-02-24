package com.khumu.alimi.data;

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
}
