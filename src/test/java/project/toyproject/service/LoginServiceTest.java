package project.toyproject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.dto.LoginDto;

import static org.assertj.core.api.Assertions.*;
import static project.toyproject.dto.MemberDto.*;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    LoginService loginService;
    @Autowired
    MemberService memberService;

    @DisplayName("로그인 성공")
    @Test
    void login() {
        //given
        Member member = createMember();
        LoginDto form = new LoginDto();
        form.setUserId("test1member");
        form.setPassword("test12345+");
        //when
        SessionMemberData login = loginService.login(form);
        //then
        assertThat(login.getUserId()).isEqualTo(form.getUserId());
        assertThat(login.getNickname()).isEqualTo(member.getNickname());
    }

    @DisplayName("로그인 실패_없는 아이디")
    @Test
    void login_idError() {
        //given
        createMember();
        LoginDto form = new LoginDto();
        form.setUserId("asdo9iudisf");
        form.setPassword("test12345+");
        //when
        SessionMemberData login = loginService.login(form);
        //then
        assertThat(login).isNull();
    }


    @DisplayName("로그인 실패_잘못된 패스워드")
    @Test
    void login_passError() {
        //given
        createMember();
        LoginDto form = new LoginDto();
        form.setUserId("test1member");
        form.setPassword("tsdfdsf45+");
        //when
        SessionMemberData login = loginService.login(form);
        //then
        assertThat(login).isNull();
    }

    @DisplayName("비밀번호 체크")
    @Test
    void passwordCheck() {
        //given
        Member member = createMember();
        //when
        Long checkMemberId = loginService.passwordCheck(member.getId(), "test12345+");
        //then
        assertThat(checkMemberId).isEqualTo(member.getId());
    }

    @DisplayName("비밀번호 체크_실패")
    @Test
    void passwordCheck_error() {
        //given
        Member member = createMember();
        //when
        Long checkMemberId = loginService.passwordCheck(member.getId(), "sdfdsffds+");
        //then
        assertThat(checkMemberId).isNull();
    }


    private Member createMember() {
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        return memberService.join(member);
    }
}