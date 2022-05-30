package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.domain.Order;
import project.toyproject.domain.OrderProduct;
import project.toyproject.domain.Product;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.OrderRepository;
import project.toyproject.repository.ProductRepository;

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
        Product product = productRepository.findOneProduct(productId);

        //예약상품 생성
        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice());

        //예약 생성
        Order order = new Order().createOrder(member, orderProduct);

        //예약 저장
        orderRepository.addOrder(order);
        return order.getId();
    }
}
