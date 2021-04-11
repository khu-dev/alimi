package com.khumu.alimi.data.dto;

import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;


/**
 * Django가 이용하는 Comment DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDto {
    Long id;
    SimpleKhumuUser author;
    Long article;
    String content;
//    Long parent;
//    List<CommentDto> children;
}