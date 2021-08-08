package com.khumu.alimi.repository;

import com.khumu.alimi.data.dto.AnnouncementAuthorFollowDto;

import java.util.List;

public interface AnnouncementAuthorFollowRepository {
    // 특정 공지사항 작성자에 대한 팔로우 정보들을 조회합니다.
    List<AnnouncementAuthorFollowDto> findAllByAuthorName(String authorName);
}
