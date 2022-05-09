package project.toyproject.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {

    @Id @GeneratedValue
    private Long memberId; //시퀀스

    private String email; //이메일(아이디)
    private String nickname; //닉네임
    private int pass;
    private String name;
    private int hp;

    @Embedded
    private Address address;

}
