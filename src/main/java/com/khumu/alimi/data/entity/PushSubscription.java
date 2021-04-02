package com.khumu.alimi.data.entity;

import com.khumu.alimi.data.entity.SimpleKhumuUser;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_pushsubscription")
@Data
@Builder
public class PushSubscription {
    @Id
    String deviceToken;

    @OneToOne
    @JoinColumn(name="user_id")
    SimpleKhumuUser user;
}
