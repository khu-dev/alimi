package com.khumu.alimi.repository;

import com.khumu.alimi.data.dto.AnnouncementAuthorFollowDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementAuthorFollowRepositoryImpl implements AnnouncementAuthorFollowRepository {
    @Override
    public List<AnnouncementAuthorFollowDto> findAllByAuthorName(String authorName) {
        return List.of(
                AnnouncementAuthorFollowDto.builder().authorName("컴퓨터공학과").followerName("bo314").build(),
                AnnouncementAuthorFollowDto.builder().authorName("컴퓨터공학과").followerName("jinsu").build(),
                AnnouncementAuthorFollowDto.builder().authorName("컴퓨터공학과").followerName("dizzi").build(),
                AnnouncementAuthorFollowDto.builder().authorName("컴퓨터공학과").followerName("gusrl4025").build()
        );
    }
}
