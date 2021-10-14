package com.khumu.alimi.external.push;

import com.khumu.alimi.data.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 이건 DB의 Entity가 아니라 FCM Push를 의미.
 */
public interface PushManager {
    void notify(Notification n, String deviceToken) throws PushException;
    // 모두에게 Notify
    /// void notify(Notication n);
    public static class PushException extends Exception{
        public PushException(String message) {
            super(message);
        }
    }
}
