package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.MemberRepository;
import project.toyproject.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberRepository memberRepository;
    private final ProductService productService;

    /**
     * 홈화면 로그인 회원 / 비로그인 회원 구분해서 홈메인 화면 처리하기
     *
     * 방법1. 파라미터로 HttpServletRequest request를 받아서 사용하기
     *         HttpSession session = request.getSession(false);//맨 처음 홈화면들어올 때 굳이 세션을 생성할 필요가 없으니 FALSE로
     *         if (session == null) { //로그인 상태가 아니면
     *             return "home";
     *         }
     *
     *         MemberDto.MemberData loginMember = (MemberDto.MemberData) session.getAttribute("loginMember");
     * ======================================================================================================================
     * 방법2. 파라미터로 @SessionAttribute 라는 스프링이 제공하는 Annotation 사용해서 SessionData
     * @SessionAttribute(name = LoginController.LOGIN_MEMBER, required = false) MemberDto.SessionMemberData loginMember,
     * ======================================================================================================================*
     * 방법3. 커스텀 어노테이션 (ArgumentResolver 사용) 만들어서 사용하기
     */
    @RequestMapping("/")
    public String home(
            @LoginCheck MemberDto.SessionMemberData loginMember, Model model) {

        //메인화면에 상품 리스트 출력
        List<Product> products = productService.findProducts();

        model.addAttribute("products", products);

        log.info("login check = {}", loginMember);
        //세션에 회원 데이터가 없으면
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);



        return "home";
    }

}
