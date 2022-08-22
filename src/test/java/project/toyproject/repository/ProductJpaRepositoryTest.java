package project.toyproject.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.toyproject.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductJpaRepositoryTest {

    @Autowired private ProductJpaRepository productJpaRepository;

    @DisplayName("상품 저장")
    @Test
    void save() {
        Product product = createProduct();

        Product saveProduct = productJpaRepository.save(product);

        assertThat(product).isEqualTo(saveProduct);
    }

    @DisplayName("단일 상품 조회")
    @Test
    void findById() {
        Product product = createProduct();
        Product saveProduct = productJpaRepository.save(product);

        Product findProduct = productJpaRepository.findById(saveProduct.getId()).get();
        assertThat(findProduct).isEqualTo(saveProduct);
    }

    @DisplayName("전체 상품 조회")
    @Test
    void findAll() {
        Product product = createProduct();
        Product save = productJpaRepository.save(product);
        Member member = createMember();
        Product product2 = Product.createProduct("test2", "tt.jpg", "test2", 20000, member, CategoryList.BOOKS);
        productJpaRepository.save(product2);

        List<Product> all = productJpaRepository.findAll();

    }

    @DisplayName("상품 삭제")
    @Test
    void deleteById() {
        Product product = createProduct();
        Product save = productJpaRepository.save(product);

        productJpaRepository.deleteById(save.getId());

        //삭제되면 아래 값이 없다.
        Optional<Product> byId = productJpaRepository.findById(save.getId());

        assertThat(byId.isEmpty()).isEqualTo(true);
    }

    @DisplayName("내가 올린 상품 리스트 보기")
    @Test
    void findProductByMemberId() {
        Member member = createMember();
        Product product2 = Product.createProduct("test2", "tt.jpg", "test2", 20000, member, CategoryList.BOOKS);
        productJpaRepository.save(product2);

        List<Product> findProducts = productJpaRepository.findProductByMemberId(member.getId());

        assertThat(findProducts.size()).isEqualTo(1);
    }


    public static Product createProduct() {
        Member member = createMember();
        return Product.createProduct("test", "tt.jpg", "test", 20000, member, CategoryList.BOOKS);
    }


    private static Member createMember() {
        return new Member("AAA1", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
    }
}