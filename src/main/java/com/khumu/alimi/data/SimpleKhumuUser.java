package com.khumu.alimi.data;

import lombok.*;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_khumuuser")
@ToString
public class SimpleKhumuUser {
    @Id
    String username;

    @Override
    public String toString() {
        return "SimpleKhumuUser{" +
                "username='" + username + '\'' +
                '}';
    }
}
