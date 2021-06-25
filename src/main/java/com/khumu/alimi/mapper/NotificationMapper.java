package com.khumu.alimi.mapper;

import com.khumu.alimi.data.dto.NotificationDto;
import com.khumu.alimi.data.dto.ResourceNotificationSubscriptionDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class NotificationMapper {
    public abstract ResourceNotificationSubscriptionDto toDto(ResourceNotificationSubscription subscription);
    public abstract NotificationDto toDto(Notification notification);
}
