package project.toyproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Order;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    /**
     * 상품 예약
     */
    public void addOrder(Order order) {
        em.persist(order);
    }

    /**
     * 상품 조회(단건)
     */
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * TODO
     * 예약한 상품 검색 + 예약한 전체 상품 리스트
     */
    public List<Order> findAll(String userId) {
        return em.createQuery("select o from Order o where o.member.userId = :userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
