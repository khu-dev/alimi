package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * https://stackoverflow.com/a/11881203/9471220
 * custom jpa repository 이용하기.
  */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification  n where n.recipientObj.username=:recipient order by n.id desc")
    List<Notification> list (@Param("recipient") String recipient);
}