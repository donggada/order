package com.teamFresh.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.teamFresh.order.exception.ErrorCode.INVALID_QUANTITY;

@Getter
@AllArgsConstructor
public class OutOfStockException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;
    private final String reason;

    public static OutOfStockException build(Object ...args) {
        return new OutOfStockException(INVALID_QUANTITY.getHttpStatus(), INVALID_QUANTITY.getMessage(), INVALID_QUANTITY.getReason().formatted(args));
    }

}
