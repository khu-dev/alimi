package com.khumu.alimi.data.dto;

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
public class CommentDto {
    Long id;
    SimpleKhumuUserDto author;
    Long article;
    String content;
//    Long parent;
//    List<CommentDto> children;
}