package com.khumu.alimi.controller;

import com.khumu.alimi.service.KhumuException;
import com.khumu.alimi.service.KhumuException.NoPermissionException;
import com.khumu.alimi.service.KhumuException.UnauthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class Advice {

    /**
     * Exception 발생 시에 수행할 작업
     *
     * @param e
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity custom(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DefaultResponse<Exception>(e.getMessage(), null));
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity NoPermission(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DefaultResponse<Exception>(e.getMessage(), null));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity UnauthenticatedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DefaultResponse<Exception>(e.getMessage(), null));
    }
}

