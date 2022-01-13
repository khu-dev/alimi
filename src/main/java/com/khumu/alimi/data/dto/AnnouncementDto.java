package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * notice crawler가 새로운 공지사항을 crawl한 경우 매시지를 publish
{
    "id": 1,
    "title": "소방시설 보완공사 시행 안내 ",
    "sub_link": "http://ce.khu.ac.kr/index.php?hCode=BOARD&page=view&idx=2330&bo_idx=1",
    "date": "2021-08-06 11:15:38",
    "author": {
        "id": 1,
        "author_name": "컴퓨터공학과",
        "followed": false
    }
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementDto {
    Long id;
    String title;
    String subLink;
//    LocalDateTime date;
    AuthorDto author;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class AuthorDto {
        Long id;
        String authorName;
        Boolean followed;
    }

}


