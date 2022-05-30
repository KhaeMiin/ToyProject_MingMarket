package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.repository.ProductRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ProductServiceTest {

    @Autowired ProductService productService;
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

        //when
        Long productId = productService.saveProduct(member.getId(), title, thumbnail, intro);

        //then
        Product getProduct = productRepository.findOneProduct(productId);
        assertThat(title).isEqualTo(getProduct.getTitle());
        assertThat(thumbnail).isEqualTo(getProduct.getThumbnail());
        assertThat(intro).isEqualTo(getProduct.getIntro());

    }

    private Member createMember() {
        Address address = new Address("city", "street", "000-000");
        Member member = new Member("test", "min", 1234, "해민", 01000000000, address);
        em.persist(member);
        return member;
    }
}