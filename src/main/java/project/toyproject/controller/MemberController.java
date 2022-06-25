package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.MemberDto;
import project.toyproject.service.MemberService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static project.toyproject.dto.MemberDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new CreateMemberForm());
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberForm") CreateMemberForm form, BindingResult result) { //form 안에 에러가 있으면 튕겨내지말고 result에 담음

        if (!form.getPassword().equals(form.getPasswordCheck())) {
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
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
        List<SelectMemberData> listMemberData = new ArrayList<>();
        for (Member member : members) {
            SelectMemberData selectMemberData = new SelectMemberData(member.getId(), member.getUserId(),
                    member.getUsername(),member.getNickname(), member.getHp(), member.getAddress());
            listMemberData.add(selectMemberData);
        }
        model.addAttribute("members", listMemberData);
        return "members/list";
    }

    /**
     * 마이페이지
     */
    @GetMapping("/myPage/{memberId}")
    public String myPage(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOneMember(memberId);
        SelectMemberData memberData = new SelectMemberData(member.getId(), member.getUserId(), member.getUsername(), member.getNickname(), member.getHp(), member.getAddress());
        model.addAttribute("member", memberData);
        return "members/myPage";
    }

    /**
     * 회원정보 수정
     */
    @GetMapping("/{memberId}/editInformation")
    public String editInformationForm(@PathVariable("memberId") Long memberId, Model model) {
        Member findMember = memberService.findOneMember(memberId);
        UpdateMemberForm memberForm = new UpdateMemberForm(findMember.getNickname(),
                findMember.getUsername(), findMember.getHp(), findMember.getAddress().getAddress(), findMember.getAddress().getDetailedAddress());
        model.addAttribute("memberForm", memberForm);
        return "members/updateMemberForm";
    }

    @PostMapping("/{memberId}/editInformation")
    public String editInformation(@PathVariable Long memberId,
                                  @Valid @ModelAttribute("memberForm") UpdateMemberForm form,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes
    ) {
        Member findMember = memberService.findOneMember(memberId);
        if (!form.getPass().equals(findMember.getPass())) { //비밀번호가 일치하지 않으면
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
            return "members/updateMemberForm";
        }

        if (result.hasErrors()) {
            return "members/updateMemberForm";
        }

        memberService.editInformation(memberId, form);

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/myPage/{memberId}";
    }

    /**
     * 비밀번호 수정
     */
    @GetMapping("/{memberId}/editPassword")
    public String editPasswordForm(@PathVariable("memberId") Long memberId, Model model) {
        model.addAttribute("passwordForm", new UpdateUserPassForm());
        return "members/updatePasswordForm";
    }

    @PostMapping("/{memberId}/editPassword")
    public String editPassword(@PathVariable Long memberId,
                               @Valid @ModelAttribute("passwordForm") UpdateUserPassForm form,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        // 현재 비밀번호 일치 확인
        Member findMember = memberService.findOneMember(memberId);
        if (!form.getPass().equals(findMember.getPass())) { //비밀번호가 일치하지 않으면
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
            return "members/updatePasswordForm";
        }

        // 변경 비밀번호 (재확인 비밀번호) 일치 확인
        if (!form.getEditYourPassword().equals(form.getEditPasswordCheck())) {
            result.reject("passwordFail2", "변경할 비밀번호가 일치하지 않습니다.");
        }
        if (result.hasErrors()) {
            return "members/updatePasswordForm";
        }

        memberService.editPassword(memberId, form);

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/myPage/{memberId}";
    }
}
