package project.toyproject.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id @GeneratedValue
    private Long orderId;

    @ManyToOne
    private Member member; //FK (현재 클래스가 연관관계 주인) : 회원 한명은 여러개의 주문을 할 수 있다.
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {
    }
}
