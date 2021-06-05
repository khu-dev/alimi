package com.khumu.alimi.mapper;

//import com.khumu.alimi.data.dto.CommentDto;
//import com.khumu.alimi.data.entity.Article;
//import com.khumu.alimi.data.entity.Comment;
//import org.mapstruct.AfterMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.MappingTarget;
//
//@Mapper
//public abstract class CommentMapper {
//    @AfterMapping
//    public CommentDto afterToDto(Comment comment, @MappingTarget CommentDto commentDto) {
//        commentDto.setArticle(comment.getArticle().getId());
//
//        return commentDto;
//    }
//
//    public Long map(Article article) {
//        return article.getId();
//    }
//
//    public abstract CommentDto toDto(Comment comment);
//}
