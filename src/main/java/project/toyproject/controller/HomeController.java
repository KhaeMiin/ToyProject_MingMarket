package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String home(
            @SessionAttribute(name = LoginController.LOGIN_MEMBER, required = false) MemberDto.SessionMemberData loginMember,
            Model model) {

/*
        //파라미터값으로 바로 확인해서 받을 수 있도록 코드 리팩토링 완료함.
        HttpSession session = request.getSession(false);//맨 처음 홈화면들어올 때 굳이 세션을 생성할 필요가 없으니 FALSE로
        if (session == null) { //로그인 상태가 아니면
            return "home";
        }

        MemberDto.MemberData loginMember = (MemberDto.MemberData) session.getAttribute("loginMember");
*/


        //세션에 회원 데이터가 없으면
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "home";
    }
}
