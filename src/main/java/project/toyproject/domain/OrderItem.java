package project.toyproject.domain;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id @GeneratedValue
    private Long orderItemId;
    private int orderPrice;
    private int count;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
