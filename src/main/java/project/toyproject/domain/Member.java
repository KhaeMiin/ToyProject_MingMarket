package project.toyproject.domain;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId; //시퀀스

    private String email; //이메일(아이디)

    @Column(length = 10)
    private String nickname; //닉네임

    @Column(length = 32) //최대 길이 정해두기
    private int pass;
    private String name;
    private int hp;

    @Embedded
    private Address address;

}
