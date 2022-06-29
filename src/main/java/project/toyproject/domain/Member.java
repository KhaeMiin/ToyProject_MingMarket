package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //시퀀스

    private String role; //권한(user, admin)

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

    public void userRole() {
        role = Role.USER.getValue();
    }

    public void adminRole() {
        role = Role.ADMIN.getValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : role.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
