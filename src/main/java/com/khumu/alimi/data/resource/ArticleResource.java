package com.khumu.alimi.data.resource;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Django가 생성한 ArticleModel을 직렬화한 것
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArticleResource {
    Long id;
    String title;
    String author;
}