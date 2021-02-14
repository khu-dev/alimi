package com.khumu.alimi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    int id;
    String kind;
    String state;
    String author;
    SimpleKhumuUser authorObj;
    int article;
    Article articleObj;
    int parent;
}
