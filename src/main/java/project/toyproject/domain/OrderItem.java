package project.toyproject.domain;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id @GeneratedValue
    private Long orderItemId;
    private int orderPrice;
    private int count;

    /**
     * @ManyToOne(fetch = FetchType.LAZY) //멤버 클래스만 DB에서 조회할 수 있도록 (굳이 조인할 필요 없이) > Team은 프록시로 가져온다.
     * @ManyToOne(fetch = FetchType.EAGER) //즉시로딩 > 바로 가져옴(member, team 바로 조인해서 가져온다)_ Member조회시 Team도 조회가능
     * 실무에서는 가급적 지연 로딩만 사용
     * 즉시로딩 적용하면 예상치 못한 SQL 발생
     * @ManyToOne, @OneToOne : 기본이 지연로딩 (LAZY로 설정하기)
     * @OneToMany, @ManyToMany : 기본이 지연로딩
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
