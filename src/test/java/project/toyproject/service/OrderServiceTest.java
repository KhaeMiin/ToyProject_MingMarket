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
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired
    EntityManager em;

    /**
     * 상품 예약하기
     */
    @Test
    void 상품예약() {
        //given
        Member member = createMember();//주문자

        //판매자
        Address address = new Address("city", "street", "000-000");
        Member member2 = new Member("admin", "admin", 1234, "admin", 01000000000, address);
        em.persist(member2);

        Product product = new Product("a", "b", "text", 10000, member2);
        em.persist(product);

        //when
        Long orderId = orderService.order(member.getId(), product.getId());

        //then
        Order getOrder = orderService.findOneOrder(orderId);
        assertEquals(getOrder.getMember(), member);
        assertEquals(getOrder.getStatus(), OrderStatus.RESERVATION); //주문상태가 예약으로 바뀌었는지

    }

    private Member createMember() {
        Address address = new Address("city", "street", "000-000");
        Member member = new Member("test", "min", 1234, "해민", 01000000000, address);
        em.persist(member);
        return member;
    }
}