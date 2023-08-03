package com.example.stockproject.exception.impl;


import com.example.stockproject.exception.AbstractException;
import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest;

public class NoCompanyException extends AbstractException {

    @Override
    public int getStatusCode() {
        // 존재하지 않는 회사명을 요청하므로 BAD REQUEST
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사명입니다.";
    }
}
