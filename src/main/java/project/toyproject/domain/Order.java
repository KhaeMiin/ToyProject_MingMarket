package project.toyproject.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id @GeneratedValue
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY) //어떤 관계인지
    @JoinColumn(name = "member_id") //Join할 컬럼 명
    private Member member; //FK (현재 클래스가 연관관계 주인) : 회원 한명은 여러개의 주문을 할 수 있다.
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //현재 주문 상태

    public Order() {
    }
}
