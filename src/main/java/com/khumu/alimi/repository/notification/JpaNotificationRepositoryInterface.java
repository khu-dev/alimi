package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.aspectj.weaver.ast.Not;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * https://stackoverflow.com/a/11881203/9471220
 * custom jpa repository 이용하기.
  */
public interface JpaNotificationRepositoryInterface extends JpaRepository<Notification, Long> {
    @Query("select n from Notification  n where n.recipient.username=:recipient")
    List<Notification> list (@Param("recipient") String recipient);
}