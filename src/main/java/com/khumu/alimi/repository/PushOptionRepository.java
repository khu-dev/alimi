package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
public interface PushOptionRepository extends JpaRepository<PushOption, String> {
}
