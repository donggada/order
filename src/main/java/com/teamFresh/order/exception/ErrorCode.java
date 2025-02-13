package com.teamFresh.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_PRODUCT(HttpStatus.CONFLICT, "존재하지 않는 상품입니다.", "product_id: %d"),
    INVALID_ORDER(HttpStatus.CONFLICT, "존재하지 않는 주문입니다.", "order_id: %d"),
    REDIS_LOCK_ERROR(HttpStatus.CONFLICT, "Redis Lock 에러입니다.", "Redis Lock 에러입니다. lock type: %s"),
    REDIS_LOCK_WAIT(HttpStatus.CONFLICT, "주문수집중 입니다.", "주문수집중 입니다. lock type: %s"),
    INVALID_QUANTITY(HttpStatus.CONFLICT, "재고가 부족합니다.","수량을 초과합니다. product_id: %d, totalQuantity : %s, orderQuantity: %s"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러입니다.", "서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;
    private final String reason;

    public ApplicationException build(Object ...args) {
        return new ApplicationException(httpStatus,httpStatus.toString(), message, reason.formatted(args));
    }
}
