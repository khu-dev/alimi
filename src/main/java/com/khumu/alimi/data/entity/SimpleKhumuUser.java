package com.khumu.alimi.data.entity;

import lombok.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_khumuuser")
@Data
@Builder
public class SimpleKhumuUser implements UserDetails {
    @Id
    String username;

    /**
     * 얘넨 다 UserDetails로 사용하기 위한 dummy implementaions
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "123123";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
