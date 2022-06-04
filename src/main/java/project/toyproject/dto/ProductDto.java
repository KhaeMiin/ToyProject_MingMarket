package project.toyproject.dto;

import lombok.Getter;
import lombok.Setter;
import project.toyproject.domain.CategoryList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProductDto {

    /**
     * 상품 등록
     */
    @Getter @Setter
    public static class createProductForm {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "대표 이미지를 등록해주세요")
        private String thumbnail;

        @NotBlank(message = "상품 설명을 작성해주세요")
        private String intro;

        @NotBlank(message = "상품 가격을 입력해주세요")
        private int price;

        @NotBlank(message = "카테고리를 선택해주세요")
        private CategoryList categoryList;

//        private Long memberId;
    }

    @Getter
    @Setter
    public static class updateProductForm {

        private Long id; //pk

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "대표 이미지를 등록해주세요")
        private String thumbnail;

        @NotBlank(message = "상품 설명을 작성해주세요")
        private String intro;

        @NotBlank(message = "상품 가격을 입력해주세요")
        private int price;

        @NotBlank(message = "카테고리를 선택해주세요")
        private CategoryList categoryList;

    }
}
