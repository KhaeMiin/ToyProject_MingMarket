package project.toyproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Product;
import project.toyproject.domain.ProductStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductDto {

    /**
     * 상품 등록
     */
    @Getter @Setter
    public static class CreateProductForm {

        private Long memberId;

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        private MultipartFile thumbnail;

        @NotBlank(message = "상품 설명을 작성해주세요")
        private String intro;

        @NotNull(message = "상품 가격을 입력해주세요")
        @Range(min = 1000, max = 99999999, message = "1,000 ~ 99,999,999원으로 다시 입력해주세요")
        private int price;

        @NotNull(message = "카테고리를 선택해주세요")
        private CategoryList categoryList;

    }

    /**
     * 상품 수정
     */
    @Getter
    @Setter
    public static class UpdateProductForm {

        private Long productId; //pk

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        private String uploadFileName;

        private MultipartFile uploadFile;

        @NotBlank(message = "상품 설명을 작성해주세요")
        private String intro;

        @NotNull(message = "상품 가격을 입력해주세요")
        @Range(min = 1000, max = 99999999, message = "1,000 ~ 99,999,999원으로 다시 입력해주세요")
        private int price;

        @NotNull(message = "카테고리를 선택해주세요")
        private CategoryList categoryList;

        public UpdateProductForm(Long productId, String title, String thumbnail, String intro, int price) {
            this.productId = productId;
            this.title = title;
            this.uploadFileName = thumbnail;
            this.intro = intro;
            this.price = price;
        }
    }

    /**
     * 상품 상세페이지
     */
    @Getter @Setter
    @AllArgsConstructor
    public static class ProductDetailData {

        private Long productId;

        private String nickName; //작성자 닉네임

        private String userId; //작성자 ID

        private String title; // 글 제목

        private String thumbnail; //대표 이미지

        private String intro; //상세설명

        private int price; //가격

        private String createDate;

//        private List<> //댓글 출력


    }

    /**
     * 상품 출력
     */
    @Getter
    @AllArgsConstructor
    public static class SelectProducts {

        private Long id; //상품 pk

        private String title; //제목
        private String thumbnail; //섬네일
        private String intro; //설명(게시판)
        private int price; //상품가격
        private Long memberId; //작성자 pk
        private ProductStatus productStatus;

        public SelectProducts(Product product) {
            id = product.getId();
            title = product.getTitle();
            thumbnail = product.getThumbnail();
            intro = product.getIntro();
            price = product.getPrice();
            memberId = product.getMember().getId();
            productStatus = product.getProductStatus();
        }
    }
}
