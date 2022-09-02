package project.toyproject.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.dto.MemberDto;
import project.toyproject.dto.ProductDto;
import project.toyproject.service.FileUpload;
import project.toyproject.service.ProductService;
import project.toyproject.service.WishItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
//    @GetMapping("/list")
    public ResultList productList() {
        List<SelectProducts> products = productService.findProducts();
        return new ResultList<>(products.size(), products);
    }

    /**
     * 전체 상품 조회
     * Spring Data JPA에서 페이징처리하기 (최근 등록 상품 순서로 출력)
     * Page<T>, Pageable, PagingAndSortingRepository
     */
    @GetMapping("/list")
    public Page<SelectProducts> list(@PageableDefault(size = 6, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.findProductsPage(pageable);
    }

    //제네릭<>을 이용하여 Object로 한 번 감싸주어 JSON에서 바로 배열로 나가버리는 것을 막는다. (필드 확장 가능해짐)
//    @GetMapping("/list")
    public ResultList list2(@PageableDefault(size = 6, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SelectProducts> products = productService.findProductsPage(pageable);
        return new ResultList(products.getTotalPages(), products);
    }

    /**
     * 내가 올린 상품 조회
     */
    @GetMapping("/products/{memberId}")
    public ResultList userProductList(@PathVariable Long memberId) {
        List<SelectProducts> userProducts = productService.userProductsList(memberId);
        return new ResultList<>(userProducts.size(), userProducts);
    }

    /**
     * 관심 상품
     */
    @GetMapping("/wish/{memberId}")
    public ResultList userWishList(@PathVariable Long memberId) {
        List<SelectProducts> userWishList = wishItemService.wishList(memberId);
        return new ResultList<>(userWishList.size(), userWishList);
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

    /**
     * 상품 상세페이지
     */
    @GetMapping("/detail/{productId}")
    public DetailProduct productDetail(@PathVariable Long productId, HttpServletRequest request) {
        ProductDetailData singleProduct = productService.findSingleProduct(productId);

        //찜상품인지 체크
        HttpSession session = request.getSession(false);
        try {
            MemberDto.SessionMemberData loginMember = (MemberDto.SessionMemberData) session.getAttribute("loginMember");
            Long wishItem = wishItemService.findOneWishItem(loginMember.getMemberId(), productId);
            if (wishItem != null) {
                return new DetailProduct<>(singleProduct, "찜상품 입니다.");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return new DetailProduct<>(singleProduct, "");

    }

    @Getter
    @AllArgsConstructor
    static class DetailProduct<T> {
        private T productData;
        private String wishStatus;
    }


    @Getter
    @AllArgsConstructor
    static class ResultList<T> {
        private int total; //페이지수, 혹은 상품갯수
        private T productData;
    }
}
