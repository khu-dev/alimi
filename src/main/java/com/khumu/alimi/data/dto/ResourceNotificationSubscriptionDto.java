package com.khumu.alimi.data.dto;

import com.khumu.alimi.data.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResourceNotificationSubscriptionDto {
    // 현재 구독 자체의 아이디
    // API 요청자는 id는 필요 없다. 혼란 방지로 없앰
    // Long id;
    // 구독하는 리소스의 아이디
    Long resourceId;
    ResourceKind resourceKind;
    String subscriber;
    @Builder.Default
    Boolean isActivated = true;
}
