package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //시퀀스

    private String userId; //이메일(아이디)

    @Column(length = 10)
    private String nickname; //닉네임

    private String pass;
    private String username;
    private int hp;

    /**
     * @Embedded: 내장타입?
     * 재사용이 가능함
     */
    @Embedded
    private Address address;

    public Member(String userId, String nickname, String pass, String username, int hp, Address address) {
        this.userId = userId;
        this.nickname = nickname;
        this.pass = pass;
        this.username = username;
        this.hp = hp;
        this.address = address;
    }

    // 회원정보 수정메서드
    public void change(String nickname, String username, int hp, Address address) {
        this.nickname = nickname;
        this.username = username;
        this.hp = hp;
        this.address = address;
    }

    //비밀번호 수정메서드
    public void passwordChange(String pass) {
        this.pass = pass;
    }

    /**
     * 비밀번호를 암호화
     * @param passwordEncoder
     * @return
     */
    public Member hashPassword(PasswordEncoder passwordEncoder) {
        this.pass = passwordEncoder.encode(this.pass);
        return this;
    }

    /**
     * 비밀번호 확인
     * @param plainPassword 암호화 이전의 비밀번호
     * @param passwordEncoder 암호화에 사용된 클래스
     * @return true/ false
     */
/*    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.pass);
    }*/


}
