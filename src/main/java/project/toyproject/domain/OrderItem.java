package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private int orderPrice;
    private int count; //주문 수량

    /**
     * @ManyToOne(fetch = FetchType.LAZY) //멤버 클래스만 DB에서 조회할 수 있도록 (굳이 조인할 필요 없이) > Team은 프록시로 가져온다.
     * @ManyToOne(fetch = FetchType.EAGER) //즉시로딩 > 바로 가져옴(member, team 바로 조인해서 가져온다)_ Member조회시 Team도 조회가능
     * 실무에서는 가급적 지연 로딩만 사용
     * 즉시로딩 적용하면 예상치 못한 SQL 발생
     * @ManyToOne, @OneToOne : 기본이 지연로딩 (LAZY로 설정하기)
     * @OneToMany, @ManyToMany : 기본이 지연로딩
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void createOrder(Order order) { //무분별한 set 막기위해서 (자칫 잘못 쓰면 어디서 잘못된 set 찾는게 너무 힘듬) createOrder 메서드로 만듬
        this.order = order;
    }
}
