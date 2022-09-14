package project.toyproject.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.toyproject.service.LoginService;
import project.toyproject.service.MemberService;

import javax.validation.Valid;

import java.util.List;

import static project.toyproject.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    private final LoginService loginService;

    /**
     * 전체 회원 조회
     */
    @GetMapping("/list")
    public ResultList memberList() {
        List<SelectMemberData> members = memberService.findMembers();
        return new ResultList<>(members.size(), members);
    }

    /**
     * 기본 회원가입
     * @return
     */
    @PostMapping("/join")
    public Long joinMember(@RequestBody @Valid CreateMemberForm form) {
        Long id = memberService.join(form).getId();
        return id;
    }

    /**
     * 회원 수정 (비밀번호 제외 회원 정보만 수정)
     * PUT: 전체 업데이트 시
     * POST, PATCH: 부분 업데이트 시
     */
    @PatchMapping("/update/{id}")
    public SelectMemberData updateMember(@PathVariable("id") Long id,
                                         @RequestBody @Valid UpdateMemberForm form) {
        return memberService.updateMember(id, form);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/editPassword/{id}")
    public String editPasswordForm(@PathVariable("id") Long id,
                                   @RequestBody @Valid UpdateUserPassForm form) {

        //기존 비밀번호 체크
        Long member = loginService.passwordCheck(id, form.getPass());
        if (member == null) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다");
        }
        //일치할 경우 새로운 비밀번호로 변경
        memberService.editPassword(id, form);
        return "비밀번호가 변경되었습니다!";
    }

    /**
     * 제네릭<>을 이용하여 Object로 한 번 감싸주어 JSON에서 바로 배열로 나가버리는 것을 막는다.
     * JSON에서 반환값을 바로 Array로 반환하면 확장할 수 없다. (아래 count와 같은 필드를 추가할 수 없다는 것)
     */
    @Getter
    @AllArgsConstructor
    static class ResultList<T> {
        private int count; //총 회원 인원수
        private T memberData;
    }
}
