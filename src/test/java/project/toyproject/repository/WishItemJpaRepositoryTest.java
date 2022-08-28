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
class WishItemJpaRepositoryTest {

    @Autowired private WishItemJpaRepository wishItemJpaRepository;
    @Autowired private ProductJpaRepository productJpaRepository;


    @DisplayName("상품 찜하기")
    @Test
    void save() {
        Member member = createMember();
        Product product = createProduct(member);
        WishItem wishItem = WishItem.addWishItem(member, product);
        WishItem result = wishItemJpaRepository.save(wishItem);

        assertThat(result).isEqualTo(wishItem);
    }

    @DisplayName("찜 조회(단건)")
    @Test
    void findById() {
        Member member = createMember();
        Product product = createProduct(member);
        WishItem wishItem = WishItem.addWishItem(member, product);
        WishItem save = wishItemJpaRepository.save(wishItem);

        Optional<WishItem> findWish = wishItemJpaRepository.findById(save.getId());
        WishItem result = findWish.get();

        assertThat(result).isEqualTo(save);
    }

    @DisplayName("찜상품 조회(회원pk, 상품pk)")
    @Test
    void findOneItem() {
        //given
        Member member = createMember();
        Product product = createProduct(member);
        productJpaRepository.save(product);

        WishItem wishItem = WishItem.addWishItem(member, product);
        WishItem save = wishItemJpaRepository.save(wishItem);
        //when
        WishItem result = wishItemJpaRepository.findOneItem(member.getId(), product.getId()).get();

        //then
        assertThat(result).isEqualTo(save);
    }

    @DisplayName("내가 찜한 상품 리스트")
    @Test
    void findByMemberId() {
        //given
        Member member = createMember();
        Product product = createProduct(member);
        productJpaRepository.save(product);

        WishItem wishItem = WishItem.addWishItem(member, product);
        WishItem save = wishItemJpaRepository.save(wishItem);

        //when
        List<WishItem> result = wishItemJpaRepository.findByMemberId(member.getId());

        //then
        assertThat(result.get(0)).isEqualTo(save);


    }

    @DisplayName("찜 취소")
    @Test
    void cancel() {
        Member member = createMember();
        Product product = createProduct(member);
        productJpaRepository.save(product);

        WishItem wishItem = WishItem.addWishItem(member, product);
        WishItem save = wishItemJpaRepository.save(wishItem);

        //when
        wishItemJpaRepository.deleteById(save.getId());

        //then
        Optional<WishItem> findWish = wishItemJpaRepository.findById(save.getId());
        assertThat(findWish.isEmpty()).isEqualTo(true);


    }

    public static Product createProduct(Member member) {
        return Product.createProduct("test", "tt.jpg", "test", 20000, member, CategoryList.BOOKS);
    }
    private static Member createMember() {
        return new Member("AAA1", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
    }

}