package project.toyproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.toyproject.domain.Member;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;

@Getter @Setter
public class LoginDto {

    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

}
