package com.lattels.smalltour.exception;

import com.lattels.smalltour.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResponseMessageException.class})
    protected ResponseEntity handleResponseMessageException(ResponseMessageException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(new ExceptionDTO(ex.getErrorCode()));
    }
//
//    @ExceptionHandler({RuntimeException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String

}
