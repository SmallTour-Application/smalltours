package com.lattels.smalltour.dto;

import com.lattels.smalltour.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExceptionDTO {

    private ErrorCode errorCode;
    private String message;

    public ExceptionDTO(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
