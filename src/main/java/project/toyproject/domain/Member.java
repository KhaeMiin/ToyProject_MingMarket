package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //시퀀스

    private String email; //이메일(아이디)

    @Column(length = 10)
    private String nickname; //닉네임

    @Column(length = 32) //최대 길이 정해두기
    private int pass;
    private String username;
    private int hp;

    /**
     * @Embedded: 내장타입?
     * 재사용이 가능함
     */
    @Embedded
    private Address address;

}
