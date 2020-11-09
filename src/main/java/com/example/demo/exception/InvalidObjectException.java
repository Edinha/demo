package com.example.demo.exception;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Resource failed validation")
public class InvalidObjectException extends RuntimeException {
    private final List<String> reasons;

    public InvalidObjectException(List<String> reasons) {
        this.reasons = reasons;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + StringUtils.join(this.reasons, ',');
    }
}
