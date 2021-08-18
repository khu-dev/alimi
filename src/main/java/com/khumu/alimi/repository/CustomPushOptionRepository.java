package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * class명을 PushOptionRespotiroyImpl로 설정하면
 * PushOptionRepository 인터페이스에 커스텀으로 적용한 메소드가 있을 경우
 * 프록시를 거쳐서 걔를 실행해야하는데 얘를 실행해버림.
 * 예를 들어 PushOptionRepository interface에 findAllByUser 라는 커스텀 메소드를 정의할 경우 JPA에서 자동으로
 * PushOptionRepositoryImpl 클래스를 만들고 findAllByUser 메소드를 만든다.
 * 근데 나도 똑같이 PushOptionRepositoryImpl 클래스를 만들면 JPA의 findAllByUser을 못 쓰게 될 수 있다는 의미.
 * 따라서 좀 이상해보이긴 하지만 그냥 Custom이라는 prefix를 이용 중!
 */
@Slf4j
@Component
@Transactional
public class CustomPushOptionRepository {
    @Autowired
    @Lazy
    PushOptionRepository pushOptionRepository;

    public Optional<PushOption> findById(Long id) {
        return pushOptionRepository.findById(id);
    }
    // id와 pushOptionKind를 통해 찾고, 없으면 true로 만들어서 리턴
    public PushOption getOrCreate(PushOption pushOption) {
        Optional<PushOption> optionalPushOption = pushOptionRepository.findByUsernameAndPushOptionKind(pushOption.getUsername(), pushOption.getPushOptionKind());
        PushOption result = null;
        if (optionalPushOption.isEmpty()) {
            // 기본값을 통해 PushOption 생성
            result = pushOptionRepository.save(PushOption.builder().username(pushOption.getUsername()).pushOptionKind(pushOption.getPushOptionKind()).build());
        } else {
            result = optionalPushOption.get();
        }
        return result;
    }

    public PushOption save(PushOption pushOption) {
        return pushOptionRepository.save(pushOption);
    }
}
