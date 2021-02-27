package com.khumu.alimi.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification_pushsubscription")
@ToString
public class PushSubscription {
    @Id
    String deviceToken;

    @OneToOne
    @JoinColumn(name="user_id")
    SimpleKhumuUser user;
}
