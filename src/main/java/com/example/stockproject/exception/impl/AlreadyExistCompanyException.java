package com.example.stockproject.exception.impl;

import com.example.stockproject.exception.AbstractException;
import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest;

public class AlreadyExistCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 사용자명입니다.";
    }
}
