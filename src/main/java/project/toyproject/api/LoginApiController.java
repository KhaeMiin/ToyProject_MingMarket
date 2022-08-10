package project.toyproject.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.toyproject.dto.LoginDto;
import project.toyproject.dto.MemberDto;
import project.toyproject.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static project.toyproject.dto.MemberDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginApiController {

    public static final String LOGIN_MEMBER = "loginMember";
    private final LoginService loginService;

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public String login(
            @RequestBody @Valid LoginDto form,
            HttpServletRequest request) {
        SessionMemberData loginMember = loginService.login(form);
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, loginMember);
        return "ok";
    }

}
