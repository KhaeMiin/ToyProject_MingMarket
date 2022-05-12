package project.toyproject.domain;

import javax.persistence.*;

@Entity
public class Item {

    @Id @GeneratedValue
    private Long itemId;
    private String title;
    private String thumbnail;
    private String intro;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
