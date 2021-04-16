package com.khumu.alimi.external.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// 나중엔 마치 Controller들의 URL을 바탕으로 적절한 controller를 실행하듯이
// Listener 들도 그렇게 되도록.

@Target(ElementType.TYPE)
public @interface EventListener {
}
