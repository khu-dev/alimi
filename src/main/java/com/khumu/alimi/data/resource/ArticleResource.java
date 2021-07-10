package com.khumu.alimi.data.resource;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Django가 이용하는 Comment DTO
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