package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.toyproject.domain.Member;
import project.toyproject.dto.LoginDto;
import project.toyproject.service.LoginService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form")LoginDto form) {
        return "/members/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("form") LoginDto form, BindingResult result) {
        if (result.hasErrors()) {
            return "/members/login";
        }

        Member loginMember = loginService.login(form.getUserId(), form.getPassword());

        //로그인 실패시 (null)
        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다");
            return "/members/login";
        }

        //로그인 성공시 TODO
        return "redirect:/";



    }
}
