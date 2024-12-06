package org.j1p5.domain.product.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.j1p5.domain.image.entitiy.ImageEntity;
import org.j1p5.domain.product.entity.ProductCategory;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.user.entity.UserEntity;

public record ProductResponseDetailInfo(
        SellerInfo seller,
        ProductLocationInfo productLocation,
        boolean hasBuyer,
        Long productId,
        String title,
        String content,
        int minimumPrice,
        //        Integer myPrice, // 내가 입찰한 가격
        //        Long auctionId,
        ProductCategory category,
        LocalDateTime uploadTime,
        LocalDateTime expiredTime,
        boolean isEarly,
        List<String> images, // 문자열 리스트로 이미지 URL 관리
        boolean isSeller // true이면 판매자인거 false이면 구매자인거
) {
    public static ProductResponseDetailInfo of(ProductEntity product, UserEntity user) {
        boolean isSeller = product.getUser().equals(user);
        return new ProductResponseDetailInfo(
                new SellerInfo(user.getId(), user.getNickname(), user.getImageUrl()),
                ProductLocationInfo.of(
                        product.getCoordinate(), product.getAddress(), product.getLocation()),
                product.isHasBuyer(),
                product.getId(),
                product.getTitle(),
                product.getContent(),
                product.getMinPrice(),
                // product.getAuction()
                product.getCategory(),
                product.getCreatedAt(),
                product.getExpiredTime(),
                product.isEarly(),
                product.getImageEntityList().stream().map(ImageEntity::getImageUrl).toList(),
                isSeller
        );

    }
}
