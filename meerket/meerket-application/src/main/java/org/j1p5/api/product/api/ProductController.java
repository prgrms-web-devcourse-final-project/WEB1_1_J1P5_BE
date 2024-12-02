package org.j1p5.api.product.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.converter.MultipartFileConverter;
import org.j1p5.api.product.dto.request.ProductRequestDto;

import org.j1p5.common.annotation.CursorDefault;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.domain.product.dto.ProductInfo;
import org.j1p5.domain.product.dto.ProductResponseInfo;
import org.j1p5.domain.product.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productUsecase; //생성자 주입


    /**
     * 중고물품 등록
     * @param request
     * @param images
     * @param userDetails
     * @return 200, 등록 완료 메세지
     * @author sunghyun0610
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response makeProduct(@RequestPart(name = "request") ProductRequestDto request,
                                @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                @AuthenticationPrincipal UserDetails userDetails
    )
                                 {


        // 세션이들어옴 -> 세션에있는 userid뽑아냄 ->이거 이용해서 지역인증 테이블에서 findById , 추후에 user role추가

        String userEmail = userDetails.getUsername();//유저 정보를 가져오기 위함
        log.info("userEmail" + userEmail);

        ProductInfo productInfo = ProductRequestDto.toInfo(request);
        List<File> imageFiles = new ArrayList<>();

        if (!images.isEmpty() && images != null) {
            imageFiles = MultipartFileConverter.convertMultipartFilesToFiles(images);
        }

        productUsecase.registerProduct(userEmail, productInfo, imageFiles);

        return Response.onSuccess();
    }

    /**
     * 사용자 활동지역 거리 기반 100km이내 중고물품 조회
     * @param category
     * @param keyword
     * @param cursor
     * @return 200, 등록 완료 메세지
     * @author sunghyun0610
     */
    @GetMapping
    public Response<CursorResult<ProductResponseInfo>> getProductByAroundPoint(@AuthenticationPrincipal UserDetails userDetails,
                                                                               @RequestParam(name = "category", required = false) String category,
                                                                               @RequestParam(name = "keyword", required = false) String keyword,
                                                                               @CursorDefault Cursor cursor) {
//        //Userdetails에서 pk값 바로 꺼내올 수 있게 수정한다고함.
        String userEmail = userDetails.getUsername();
        log.info("userEmail :" + userEmail);

        // 서비스 호출
        CursorResult<ProductResponseInfo> products = productUsecase.getProducts(userEmail, cursor);//cursorResult형 조회된 productResponseInfo 반환



        return Response.onSuccess(products);


    }


}
