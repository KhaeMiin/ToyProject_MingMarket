package project.toyproject.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.repository.ProductRepository;
import project.toyproject.service.ProductService;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ProductServiceTest1 {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager em;

    @Test
    void 상품_등록() {
        //given :이런게 주어지면
        Member member = createMember();
        String title = "aa";
        String thumbnail = ".jng";
        String intro = "text";
        int price = 10000;

        //when
        Long productId = productService.saveProduct(member.getId(), title, thumbnail, intro, price, CategoryList.FASHION);

        //then
        Product getProduct = productRepository.findSingleProduct(productId);
        assertThat(title).isEqualTo(getProduct.getTitle());
        assertThat(thumbnail).isEqualTo(getProduct.getThumbnail());
        assertThat(intro).isEqualTo(getProduct.getIntro());
        assertThat(price).isEqualTo(getProduct.getPrice());

    }

    private Member createMember() {
        Address address = new Address("city", "street");
        Member member = new Member("test", "min", "1234", "해민", 01000000000, address);
        em.persist(member);
        return member;
    }
}