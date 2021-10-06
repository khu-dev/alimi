package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
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
    String link;
    // follower들의 ID
    List<String> followers;
}


