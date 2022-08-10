package project.toyproject.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.dto.MemberDto;
import project.toyproject.dto.ProductDto;
import project.toyproject.service.FileUpload;
import project.toyproject.service.ProductService;
import project.toyproject.service.WishItemService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static project.toyproject.dto.ProductDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {

    private final ProductService productService;
    private final WishItemService wishItemService;
    private final FileUpload fileUpload;

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

    /**
     * 상품 등록
     */
    @GetMapping("/new")
    public Long createProduct(
            @Valid @ModelAttribute("form") CreateProductForm form,
            @LoginCheck MemberDto.SessionMemberData loginMember,
            HttpServletRequest request) throws IOException {
        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 저장 경로
        String uploadFile = fileUpload.serverUploadFile(form.getThumbnail(), realPath);

        return productService.saveProduct(loginMember.getMemberId(), form.getTitle(), uploadFile, form.getIntro(), form.getPrice(), form.getCategoryList());

    }

    @Getter
    @AllArgsConstructor
    static class ResultList<T> {
        private int count; //총 회원 인원수
        private T memberData;
    }
}
