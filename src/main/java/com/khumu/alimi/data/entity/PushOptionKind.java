package com.khumu.alimi.data.entity;

// 어떤 종류의 푸시 옵션인지
// 예를 들어 핫 게시글 선정에 대한 푸시 옵션
public enum PushOptionKind {
    // 새로 핫 게시글이 선정되었을 때
    NEW_HOT_ARTICLE,
    // 자신이 팔로우 중인 작성자의 새 공지사항이 작성됐을 때
    NEW_ANNOUNCEMENT_OF_FOLLOWING_AUTHOR,
    // 쿠뮤에서 학교 꿀팁 및 이것 저것에 대해 전달하는 알림
    KHUMU_NOTIFICATION,
    // 쿠뮤 서비스를 만들어가는 과정에 대한 알림
    KHUMU_SERVICE_NOTIFICATION
}
