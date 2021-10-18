package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * notice crawler가 새로운 공지사항을 crawl한 경우 매시지를 publish
{
   "announcement": {
     "id": 184,
     "title": "수강신청 안내",
     "author_name": "컴퓨터공학과"
   },
   "followers": [
     "jinsu", "bo314"
   ]
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewAnnouncementCrawledDto {
    AnnouncementDto announcement;
    // 알림을 받을 후보인 user들. 해당 author을 follow 중이다
    List<String> followers;
}


