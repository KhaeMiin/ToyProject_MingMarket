package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.*;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.OrderRepository;
import project.toyproject.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 예약하기
     */
    @Transactional
    public Long order(Long memberId, Long productId) {

        //엔티티 조회
        Member member = memberRepository.findOneMember(memberId);
        Product product = productRepository.findSingleProduct(productId);

        //예약상품 생성
        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice());

        //예약 생성
        Order order = Order.createOrder(member, orderProduct);


        //예약 저장
        orderRepository.addOrder(order);
        return order.getId();
    }

    /**
     * 예약 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //order 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //예약 취소
        order.cancel();
    }

    /**
     * 거래 완료
     */
    @Transactional
    public void transactionComplete(Long orderId) {
        //order 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //거래 완료
        order.transactionComplete();
    }

    /**
     * 주문 조회 (단건)
     */
    public Order findOneOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        return order;
    }

    /**
     * 주문 조회 (회원 한명이 주문한 목록 리스트)
     */
    public List<Order> findMembers(String userId) {
        return orderRepository.findAll(userId);
    }

}
