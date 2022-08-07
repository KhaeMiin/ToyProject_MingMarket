package project.toyproject.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.toyproject.dto.ProductDto;
import project.toyproject.service.ProductService;
import project.toyproject.service.WishItemService;

import java.util.List;

import static project.toyproject.dto.ProductDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {

    private final ProductService productService;
    private final WishItemService wishItemService;

    /**
     * 전체 상품 조회
     */
    @GetMapping("/list")
    public ResultList productList() {
        List<SelectProducts> products = productService.findProducts();
        return new ResultList(products.size(), products);
    }

    /**
     * 내가 올린 상품 조회
     */
    @GetMapping("/products/{memberId}")
    public ResultList userProductList(@PathVariable Long memberId) {
        List<SelectProducts> userProducts = productService.userProductsList(memberId);
        return new ResultList(userProducts.size(), userProducts);
    }

    /**
     * 관심 상품
     */
    @GetMapping("/wish/{memberId}")
    public ResultList userWishList(@PathVariable Long memberId) {
        List<SelectProducts> userWishList = wishItemService.wishList(memberId);
        return new ResultList(userWishList.size(), userWishList);
    }

    @Getter
    @AllArgsConstructor
    static class ResultList<T> {
        private int count; //총 회원 인원수
        private T memberData;
    }
}
