package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
public interface PushDeviceRepository extends JpaRepository<PushDevice, String> {
    List<PushDevice> findAllByUser(String user);
}
