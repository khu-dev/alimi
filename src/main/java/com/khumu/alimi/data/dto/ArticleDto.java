package com.khumu.alimi.data.dto;

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
public class ArticleDto {
    Long id;
    SimpleKhumuUserDto author;
}