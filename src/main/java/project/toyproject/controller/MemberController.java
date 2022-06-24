package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.MemberDto;
import project.toyproject.service.MemberService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberDto.CreateMemberForm());
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberForm") MemberDto.CreateMemberForm form, BindingResult result) { //form 안에 에러가 있으면 튕겨내지말고 result에 담음

        if (!form.getPassword().equals(form.getPasswordCheck())) {
            result.reject("passwordFail","비밀번호가 일치하지 않습니다.");
        }

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "members/joinMemberForm"; //다시 폼으로 이동
        }
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        Member member = new Member(form.getUserId(), form.getNickname(), form.getPassword(),
                form.getUsername(), form.getHp(), address);
        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 회원목록 출력 (관리자만 사용 가능)
     */
    @GetMapping("/list")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        List<MemberDto.SelectMemberData> listMemberData = new ArrayList<>();
        for (Member member : members) {
            MemberDto.SelectMemberData selectMemberData = new MemberDto.SelectMemberData(member.getId(), member.getUserId(),
                    member.getUsername(), member.getHp(), member.getAddress());
            listMemberData.add(selectMemberData);
        }
        model.addAttribute("members", listMemberData);
        return "members/list";
    }
}
