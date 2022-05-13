package project.toyproject.domain;

import javax.persistence.*;

@Entity
public class Item extends BaseEntity { //상품

    @Id @GeneratedValue
    private Long itemId;
    private String title;
    private String thumbnail;
    private String intro;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne @JoinColumn(name = "category_id")
    private Category category; //fk
}
