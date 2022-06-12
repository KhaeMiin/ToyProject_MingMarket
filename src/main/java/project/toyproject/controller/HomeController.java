package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);//맨 처음 홈화면들어올 때 굳이 세션을 생성할 필요가 없으니 FALSE로
        if (session == null) { //로그인 상태가 아니면
            return "home";
        }

        Member loginMember = (Member) session.getAttribute("loginMember");

        //세션에 회원 데이터가 없으면
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "home";
    }
}
