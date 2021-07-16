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

    @Getter
    @Setter
    @ToString
    public static class NoPermissionException extends Exception {
        public NoPermissionException() {
            super("작업을 수행할 권한이 존재하지 않습니다.");
        }
        public NoPermissionException(String message) {
            super(message);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UnauthenticatedException extends Exception {
        public UnauthenticatedException() {
            super("인증되지 않은 유저의 요청입니다.");
        }
        public UnauthenticatedException(String message) {
            super(message);
        }
    }

}
