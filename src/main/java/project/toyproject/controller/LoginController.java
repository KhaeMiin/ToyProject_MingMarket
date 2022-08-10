package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.LoginDto;
import project.toyproject.dto.MemberDto;
import project.toyproject.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static project.toyproject.dto.MemberDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_MEMBER = "loginMember";
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form")LoginDto form) {
        return "/members/login";
    }

    /**
     * @param redirectURL
     * 로그인을 하지 않은 사용자가 로그인 인증이 필요한 페이지 요청시
     * 로그인폼으로 이동 후 로그인 체크 후 다시 요청페이지로 돌아감.
     * Interceptor 설정
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("form") LoginDto form,
                        BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            return "/members/login";
        }

        SessionMemberData loginMember = loginService.login(form);

        //로그인 실패시 (null)
        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다");
            return "/members/login";
        }

        //로그인 성공처리
        //기존 세션이 있으면 세션을 반환, 없으면 새로운 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보를 보관 (쿠키에 key: JSESSIONID , value: UUID 로 들어감)
        session.setAttribute(LOGIN_MEMBER, loginMember);
        log.info("redirectURL={}",redirectURL);

        return "redirect:" + redirectURL;
    }

    //Spring Security에서 Post로 요청하는 logout에 대해 요청을 가로채서 대신 처리한다.
    // 그래서 해당 컨트롤러의 기능을 대신 수행하기 때문에 아래 핸들러는 필요없음
    // 해당 핸들러가 작동하는 기능을 변경하기 위해서는 Security 설정 어댑터에서 할 수 있다. (WebSecurityConfigurerAdapter를 상속받은 클래스: SecurityConfig)
/*    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);//있는 세션 가져오게 하기. 없으면 null 가져오기
        log.info("로그아웃 되었습니다!!!!!");
        if (session != null) {
            session.invalidate(); //데이터 날라감!
        }

        return "redirect:/"; //홈 화면으로!
    }*/
}
