package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushOptionKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
public interface PushOptionRepository extends JpaRepository<PushOption, String> {
    Optional<PushOption> findByIdAndPushOptionKind(String id, PushOptionKind pushOptionKind);
}
