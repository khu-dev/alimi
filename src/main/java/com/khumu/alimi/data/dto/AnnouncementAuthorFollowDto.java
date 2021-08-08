package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 공지사항 마이크로서비스에서 조회한 Announcement DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementAuthorFollowDto {
    String authorName;
    String followerName;
}