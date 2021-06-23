package com.khumu.alimi.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class KhumuException {
    @Getter
    @Setter
    @ToString
    public static class WrongResourceKindException extends Exception {
        public WrongResourceKindException() {
            super("ResourceKind가 올바르지 않습니다.");
        }
        public WrongResourceKindException(String message) {
            super(message);
        }
    }
}
