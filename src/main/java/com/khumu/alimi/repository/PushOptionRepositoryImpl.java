package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
@Slf4j
public class PushOptionRepositoryImpl {
    @Autowired
    @Lazy
    PushOptionRepository pushOptionRepository;

    public PushOption getOrCreate(PushOption pushOption) {
        Optional<PushOption> optionalPushOption = pushOptionRepository.findById(pushOption.getId());
        PushOption result = null;
        if (optionalPushOption.isEmpty()) {
            // 기본값을 통해 PushOption 생성
            result = pushOptionRepository.save(PushOption.builder().id(pushOption.getId()).build());
        } else {
            result = optionalPushOption.get();
        }
        return result;
    }

    public PushOption save(PushOption input) {
        return pushOptionRepository.save(input);
    }
}
