package project.toyproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import project.toyproject.domain.CategoryList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    public static class updateProductForm {

        private Long id; //pk

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
}
