package project.toyproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Order;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    // 주문하기
    public void addOrder(Order order) {
        em.persist(order);
    }

    // 주문 조회(단건)
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 전체 조회
//    public List<Order> findAll(OrderSerch orderSerch) {}

}
