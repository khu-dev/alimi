package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * Notification Entitiy를 이용하는 Repository
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification  n where n.recipient=:recipient order by n.id desc")
    Page<Notification> findAllByRecipient (@Param("recipient") String recipient, Pageable pageable);
}