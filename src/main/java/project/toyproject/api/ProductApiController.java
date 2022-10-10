package project.toyproject.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.service.CommentService;
import project.toyproject.service.FileUpload;
import project.toyproject.service.ProductService;
import project.toyproject.service.WishItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static project.toyproject.dto.CommentDto.*;
import static project.toyproject.dto.MemberDto.*;
import static project.toyproject.dto.ProductDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {

    private final ProductService productService;
    private final WishItemService wishItemService;
    private final FileUpload fileUpload;
    private final CommentService commentService;

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
        return new ResultList<>(products.getTotalPages(), products);
    }

    /**
     * 내가 올린 상품 조회
     */
    @GetMapping("/my-product")
    public ResultList userProductList(HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        List<SelectProducts> userProducts = productService.userProductsList(loginMember.getMemberId());
        return new ResultList<>(userProducts.size(), userProducts);
    }


    /**
     * 내가 올린 상품 조회(페이징 처리)
     */
    @GetMapping("/my-product-paging")
    public Page<SelectProducts> userProductList(
            @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        return productService.userProductsListPage(loginMember.getMemberId(), pageable);
    }

    /**
     * 내 관심 상품 List
     */
    @GetMapping("/my-wish")
    public ResultList userWishList(HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        List<SelectProducts> userWishList = wishItemService.wishList(loginMember.getMemberId());
        return new ResultList<>(userWishList.size(), userWishList);
    }
    /**
     * 내 관심 상품 List 페이징 처리
     */
    @GetMapping("/my-wish-paging")
    public Page<SelectProducts> userWishListPage(
            @PageableDefault(size = 6, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        return wishItemService.wishListPage(loginMember.getMemberId(), pageable);
    }

    /**
     * 상품 등록
     */
    @PostMapping("/new")
    public Long createProduct(
            @Valid @ModelAttribute("form") CreateProductForm form,
            BindingResult result,
            @LoginCheck SessionMemberData loginMember,
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
        List<CommentRequestDto> comment = commentService.findByProductId(productId);

        //찜상품인지 체크
        HttpSession session = request.getSession(false);
        try {
            SessionMemberData loginMember = (SessionMemberData) session.getAttribute("loginMember");
            Long wishItem = wishItemService.findOneWishItem(loginMember.getMemberId(), productId);
            if (wishItem != null) {
                return new DetailProduct<>(singleProduct, "찜상품 입니다.", comment);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return new DetailProduct<>(singleProduct, "", comment);
    }

    /**
     * 상품 삭제
     * TODO
     * 테스트 실행 전
     */
    @PostMapping("/delete/{productId}")
    public String removeProduct(@PathVariable Long productId, HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        ProductDetailDataV2 findProduct = productService.findSingleProductV2(productId);
        if (loginMember.getMemberId().equals(findProduct.getMemberId())) {
            productService.removeProduct(productId);
            commentService.deleteByProductId(productId);
            return "삭제가 완료되었습니다.";
        }
        return "작성자만 삭제가 가능합니다.";
    }

    /**
     * 댓글 작성
     */
    @PostMapping("/comment/{productId}")
    public String addComment(
            @RequestBody String comment,
            @PathVariable Long productId,
            HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        CommentResponseDto form = new CommentResponseDto(productId, loginMember.getMemberId(), null, comment);

        commentService.addComment(form);
        return "댓글 작성 완료!";
    }

    /**
     * 댓글 삭제 (대댓글도 함께 삭제)
     * TODO
     * 테스트 실행 전
     */
    @DeleteMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        SessionMemberData loginMember = getSessionMemberData(request);
        CommentRequestDto findComment = commentService.findByCommentId(commentId);
        if (findComment.getMemberId().equals(loginMember.getMemberId())) {
            commentService.deleteComment(commentId);
            if (findComment.getParentId() != null) { //대댓글도 삭제하기
                commentService.deleteChildComment(commentId);
            }
            return "삭제가 완료되었습니다!";
        }
        return "작성자만 삭제가 가능합니다.";

    }

    private static SessionMemberData getSessionMemberData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SessionMemberData loginMember = (SessionMemberData) session.getAttribute("loginMember");
        return loginMember;
    }



    @Getter
    @AllArgsConstructor
    static class DetailProduct<T> {
        private T productData;
        private String wishStatus;
        private T commentData;
    }


    @Getter
    @AllArgsConstructor
    static class ResultList<T> {
        private int total; //페이지수, 혹은 상품갯수
        private T productData;
    }
}
