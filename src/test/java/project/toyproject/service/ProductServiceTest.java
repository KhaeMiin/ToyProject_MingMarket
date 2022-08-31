package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
import project.toyproject.dto.ProductDto;
import project.toyproject.repository.ProductJpaRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static project.toyproject.dto.ProductDto.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired
    ProductJpaRepository productJpaRepository;
    @Autowired
    MemberService memberService;

    @DisplayName("상품 등록")
    @Test
    void saveProduct() {
        //given
        Member member = createMember();
        //when
        Long productId = productService.saveProduct(member.getId(), "test", "test.jpg", "test", 10000, CategoryList.BOOKS);
        Product product = productJpaRepository.findById(productId).get();
        //then
        assertThat(productId).isEqualTo(product.getId());
        assertThat(member).isEqualTo(product.getMember());
    }

    @DisplayName("상품 등록_실패")
    @Test
    void saveProduct_error() {
        //given
        Long memberId = null; //없는 회원
        //when
        try {
            productService.saveProduct(memberId, "test", "test.jpg", "test", 10000, CategoryList.BOOKS);
        } catch (Exception e) {
            return;
        }
        //then
        fail(); //에러가 발생하지 않았을 경우
        assertThrows(IllegalStateException.class,
                () -> productService.saveProduct(memberId, "test", "test.jpg", "test", 10000, CategoryList.BOOKS));

    }

    @DisplayName("상품 수정")
    @Test
    void updateProduct() {
        //given
        Product product = createProduct();
        UpdateProductForm form = new UpdateProductForm(product.getId(), "updateTest", "updateTest.jpg", "test", 1000);
        //when
        productService.updateProduct(product.getId(), form, form.getUploadFileName());
        Product findProduct = productJpaRepository.findById(product.getId()).get();
        //then
        assertThat(findProduct.getTitle()).isEqualTo(form.getTitle());
        assertThat(findProduct.getIntro()).isEqualTo(form.getIntro());
        assertThat(findProduct.getPrice()).isEqualTo(form.getPrice());
        assertThat(findProduct.getThumbnail()).isEqualTo(form.getUploadFileName());
    }

    private Product createProduct() {
        Member member = createMember();
        Product product = Product.createProduct("test", "test.jpg", "test", 10000, member, CategoryList.BOOKS);
        productJpaRepository.save(product);
        return product;
    }


    private Member createMember() {
        MemberDto.CreateMemberForm member = new MemberDto.CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        return memberService.join(member);
    }
}