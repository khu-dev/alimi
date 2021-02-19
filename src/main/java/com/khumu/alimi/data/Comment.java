package com.khumu.alimi.data;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    Long id;
    String kind;
    String state;
    String content;
    String authorId;
    @SerializedName("author")
    SimpleKhumuUser authorObj;
    int article;
    Article articleObj;
    int parent;
}
