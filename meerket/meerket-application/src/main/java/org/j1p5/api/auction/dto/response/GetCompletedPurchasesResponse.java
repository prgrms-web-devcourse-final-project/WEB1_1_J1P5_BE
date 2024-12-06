package org.j1p5.api.auction.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;

public record GetCompletedPurchasesResponse(
        Long productId,
        Long auctionId,
        String productTitle,
        String productImage,
        int myPrice,
        String sellerAddress,
        LocalDateTime productCreatedAt,
        int minPrice,
        LocalDateTime expireTime
) {
    public GetCompletedPurchasesResponse{
        Objects.requireNonNull(productId, "productId 는 필수입니다.");
        Objects.requireNonNull(auctionId, "auctionId 는 필수입니다.");
        Objects.requireNonNull(productTitle, "productTitle 는 필수입니다.");
        Objects.requireNonNull(productCreatedAt, "productCreatedAt 는 필수입니다.");
        Objects.requireNonNull(sellerAddress, "sellerAddress 는 필수입니다.");
        Objects.requireNonNull(expireTime, "expireTime 는 필수입니다.");
    }
}
