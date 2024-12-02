package org.j1p5.domain.product.exception;

import org.j1p5.common.exception.BaseErrorCode;
import org.j1p5.common.exception.ErrorResponse;

public enum ProductException implements BaseErrorCode {

    // 동네 인증관련 에러
    REGION_AUTH_NOT_FOUND(404, "REGION404", "사용자의 동네 인증 정보가 없습니다."),
    PRODUCT_NOT_FOUND(404, "PRODUCT404", "상품 조회에 실패하였습니다."),
    PRODUCT_NOT_AUTHORIZED(403, "PRODUCT403", "상품 수정 권한이 없습니다."),
    PRODUCT_HAS_BUYER(405, "PRODUCT450", "입찰자가 있는 상품은 수정할 수 없습니다."),
    PRODUCT_IS_DELETED(410, "PRODUCT410", "삭제된 게시물입니다.");
    private final int status;
    private final String errorCode;
    private final String message;

    ProductException(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return ErrorResponse.of(false, status, errorCode, message);
    }
}
