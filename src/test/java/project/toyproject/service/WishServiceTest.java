package project.toyproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.*;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional(readOnly = true)
class WishServiceTest {

    @Autowired
    WishItemService wishService;
    @Autowired
    EntityManager em;

    /**
     * 상품 찜하기
     */
    @Test
    void 상품찜추가() {
        //given
        Member member = createMember("test", "min", "1234", "해민", 01000000000);//주문자
        Member member2 = createMember("admin", "admin", "1234", "admin", 01000000000);//판매자

        Product product = createProduct(member2);

        //when
        Long wishId = wishService.addWishItem(member.getId(), product.getId());

        //then
        WishItem getWishItem = wishService.findOneOrder(wishId);
        assertEquals(getWishItem.getMember(), member);
        assertEquals(getWishItem.getStatus(), ProductStatus.WISH); //주문상태가 예약으로 바뀌었는지

    }

    @Test
    void 예약취소() {
        //given
        Member member = createMember("aaa", "name", "1234", "kim", 01000000000);
        Product product = createProduct(member);

        Long wishId = wishService.addWishItem(member.getId(), product.getId());

        //when
        wishService.cancelWishItem(wishId);

        //then
        WishItem getWishItem = wishService.findOneOrder(wishId);

        assertThrows(NullPointerException.class, () -> getWishItem.getMember()); //찜 상품에서 삭제되었으니까 null
        assertThrows(NullPointerException.class, () -> getWishItem.getProduct());
    }

    private Member createMember(String userId, String name, String pass, String username, int hp) {
        Address address = new Address("city", "street");
        Member member = new Member(userId, name, pass, username, hp, address);
        em.persist(member);
        return member;
    }

    private Product createProduct(Member member2) {
        Product product = Product.createProduct("a", "b", "text", 10000, member2);
        em.persist(product);
        return product;
    }

}