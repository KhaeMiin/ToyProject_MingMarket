package project.toyproject.domain;

import java.time.LocalDateTime;

public class Order {

    private Long orderId;
    private Member member; //FK
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
}
