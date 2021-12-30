package com.khumu.alimi.data.dto;

import lombok.*;

/**
 * 이벤트 메시지로 전달받는 유저 정보. 이 중 필요한 필드만 사용한다.
 {
    "last_login": null,
    "is_active": true,
    "date_joined": "2021-12-03T03:27:35.493",
    "username": "jinsu.dev",
    "kind": "guest",
    "nickname": "\\uac1c\\ubc1c\\ud558\\ub294 \\ud638\\ub791\\uc774",
    "student_number": "2000123123",
    "department": "\\ud559\\uacfc \\ubbf8\\uc124\\uc815",
    "created_at": "2021-12-03T03:27:35.493",
    "status": "deleted",
    "is_superuser": false,
    "profile_image": null,
    "info21_authenticated_at": null,
    "groups": [],
    "user_permissions": []
 }
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KhumuUserDto {
    private String username;

    @Override
    public String toString() {
        return "KhumuUserDto{" +
                "username='" + username + '\'' +
                '}';
    }
}


