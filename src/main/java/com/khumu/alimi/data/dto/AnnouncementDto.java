package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * notice crawler가 새로운 공지사항을 crawl한 경우 매시지를 publish
{
   "id": 184,
   "title": "수강신청 안내",
   "author_name": "컴퓨터공학과"
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementDto {
    Long id;
    String title;
    String authorName;
    String subLink; // 왜 필드 이름이 sub_link일까..
}


