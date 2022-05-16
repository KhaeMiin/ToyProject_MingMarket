package project.toyproject.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class Item extends BaseEntity { //상품

    @Id @GeneratedValue
    private Long itemId;
    private String title;
    private String thumbnail;
    private String intro;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY) @JoinColumn(name = "category_id")
    private Category category; //fk
}
