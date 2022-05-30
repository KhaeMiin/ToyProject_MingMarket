package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //어떤 관계인지
    @JoinColumn(name = "member_id") //Join할 컬럼 명
    private Member member; //FK (현재 클래스가 연관관계 주인) : 회원 한명은 여러개의 주문을 할 수 있다.
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //현재 주문 상태

    /**
     * cascade = CascadeType.ALL: Order 저장시 OrderProduct 같이 저장
     * cascade : 다른 엔티티와 연관관계가 있을 경우 사용하지 말자.
     * 소유자가 하나일 경우 사용하자. (단일 엔티티에 완전히 종속적일 경우에만)
     *  orphanRemoval = true: 그니까 부모 삭제하면 부모 PK 연결돼있는 자식도 삭제된다는거 > 부모삭제되면 자식은 고아됨. 그래서 삭제됨
     *  이것 역시 소유자가 하나일 경우에만 사용하자.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();//연관관계 편의메서드 만들어야 합니다.(엔티티에서는 양쪽에 값을 넣어주는게 맞음)

    public Order() {
    }

    public Order(Member member, LocalDateTime orderDate, OrderStatus status) {
        this.member = member;
        this.orderDate = orderDate;
        this.status = status;
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    //연관관계 편의메서드
    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.createOrder(this); //setter 생성 막기위해서 caretOrder 메서드로 만들었습니다. (무분별한 set 막기)
    }

    //생성 메서드
    public Order createOrder(Member member, OrderProduct... orderProducts) {
        if (status == OrderStatus.RESERVATION) { //주문 상태가:
            throw new IllegalStateException("예약된 상품은 예약할 수 없습니다.");
        }
        Order order = new Order(member, LocalDateTime.now(), OrderStatus.RESERVATION); //생성시(주문시) 상품상태: 예약 으로 초기화
        for (OrderProduct orderProduct : orderProducts) {
            order.addOrderProduct(orderProduct);
        }
        return order;
    }

    //비즈니스 로직
    /**
     * 예약 취소
     */
    public void cancel() {
        if (status == OrderStatus.TRANSACTION_COMPLETE) { //주문 상태가:
            throw new IllegalStateException("이미 거래가 완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
    }
}
