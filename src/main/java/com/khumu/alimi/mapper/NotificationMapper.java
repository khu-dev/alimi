package com.khumu.alimi.mapper;

import com.khumu.alimi.data.dto.NotificationDto;
import com.khumu.alimi.data.dto.ResourceNotificationSubscriptionDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;

import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Value;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class NotificationMapper {
    @Value("${khumu.notification.rootLink}")
    String NOTIFICATION_ROOT_LINK;

    @AfterMapping
    protected void afterToDto(Notification src, @MappingTarget NotificationDto dest) {
        if (src.getLink() == null && src.getReference() != null) {
            dest.setLink(NOTIFICATION_ROOT_LINK + "/" + src.getReference());
        }
    }

    public abstract ResourceNotificationSubscriptionDto toDto(ResourceNotificationSubscription subscription);
    public abstract NotificationDto toDto(Notification notification);
}
