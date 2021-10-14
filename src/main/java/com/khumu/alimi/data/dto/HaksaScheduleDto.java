package com.khumu.alimi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
{
    "id": 83,
    "starts_at": "2021-10-09T17:35:00+09:00",
    "ends_at": "2022-02-25T23:59:59.999000+09:00",
    "title": "테스트-3 - 시간 안 지남",
    "is_notified": false
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HaksaScheduleDto {
    LocalDateTime startsAt;
    LocalDateTime endsAt;
    String title;
}


