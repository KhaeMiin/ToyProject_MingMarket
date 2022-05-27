package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Item extends BaseEntity { //상품

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id; //pk

    private String title; //제목
    private String thumbnail; //섬네일
    private String intro; //설명

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //fk

    @OneToOne(fetch = LAZY) @JoinColumn(name = "category_id")
    private Category category; //fk
}
