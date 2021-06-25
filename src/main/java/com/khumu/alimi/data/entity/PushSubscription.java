package com.khumu.alimi.data.entity;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Data
@Builder
public class PushSubscription {
    @Id
    String deviceToken;
    String user;
}
