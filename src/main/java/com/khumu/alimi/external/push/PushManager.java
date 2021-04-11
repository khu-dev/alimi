package com.khumu.alimi.external.push;

import com.khumu.alimi.data.entity.Notification;
import org.springframework.stereotype.Repository;

/**
 * 이건 DB의 Entity가 아니라 FCM Push를 의미.
 */
public interface PushManager {
    void notify(Notification n, String deviceToken);
    // 모두에게 Notify
    /// void notify(Notication n);
}
