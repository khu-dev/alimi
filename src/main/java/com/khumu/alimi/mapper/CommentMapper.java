package com.khumu.alimi.mapper;

import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.entity.Comment;

public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        CommentDto dto = CommentDto.builder()
                .id(comment.getId())
                .article(comment.getId())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .build();
        return dto;
    }

//    public Comment toEntity(CommentDto dto) {
//        return Comment.builder().
//    }
}
