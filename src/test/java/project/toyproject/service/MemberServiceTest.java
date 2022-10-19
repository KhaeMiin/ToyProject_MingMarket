package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.repository.jpql.MemberJpaRepository;

import static org.junit.jupiter.api.Assertions.*;
import static project.toyproject.dto.MemberDto.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired MemberService memberService;

    @DisplayName("회원가입 성공")
    @Test
    void save() {
        //given
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");

        //when
        Member result = memberService.join(member);
        
        //then
        Assertions.assertThat(result.getUserId()).isEqualTo(member.getUserId());
    }
    
    @DisplayName("회원가입 실패 중복 아이디")
    @Test
    void save_error1() {
        //given
        createMember();
        
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        
        //when
        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            return;
        }
        
        //then
        fail("예외가 발생해야하는데 발생하지 않았습니다.");
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }

    @DisplayName("회원가입 실패 비밀번호 유요성 체크 실패")
    @Test
    void save_error2() {
        //given
        createMember();

        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test2member", "min",
                "test12", "test12", "해민", 0100000000, "성수동", "밍마켓 123-4");

        //when
        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            return;
        }

        //then
        fail("예외가 발생해야하는데 발생하지 않았습니다.");
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member));
        assertEquals("비밀번호를 다시 입력해주세요.", thrown.getMessage());
    }

    @DisplayName("회원 정보 수정")
    @Test
    void editInformation() {
        //given
        Member member = createMember();
        UpdateMemberForm form = new UpdateMemberForm("김수정","test222", member.getHp(), member.getAddress().getAddress(), member.getAddress().getDetailedAddress());
        //when
        memberService.editInformation(member.getId(), form);
        //then
        Member updateMember = memberJpaRepository.findById(member.getId()).get();

        Assertions.assertThat(updateMember.getNickname()).isEqualTo(form.getNickname());
        Assertions.assertThat(updateMember.getUsername()).isEqualTo(form.getUsername());

    }

    @DisplayName("회원정보 수정 실패")
    @Test
    void editInformation_error() {
        //given
        Member member = createMember();
        UpdateMemberForm form = new UpdateMemberForm("김수정","test222", member.getHp(), member.getAddress().getAddress(), member.getAddress().getDetailedAddress());
        //when
        try {
            memberService.editInformation(0L, form); //없는 회원 조회
        } catch (Exception e) {
            return;
        }
        //then
        fail("예외가 발생해야하는데 발생하지 않았습니다.");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> memberService.editInformation(0L, form));
        assertEquals("저장된 값이 없습니다.", ex.getMessage());
    }

    @DisplayName("비밀번호 수정")
    @Test
    void editPassword() {
        //given
        Member member = createMember();
        UpdateUserPassForm form = new UpdateUserPassForm();
        form.setPass(member.getPass());
        form.setEditYourPassword("mingmark1+");
        form.setEditPasswordCheck("mingmark1+");
        //when
        memberService.editPassword(member.getId(), form);
        //then
        Member findMember = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(member.getPass()).isEqualTo(findMember.getPass());

    }

    @DisplayName("비밀번호 수정 실패")
    @Test
    void editPassword_error() {
        //given
        Member member = createMember();
        UpdateUserPassForm form = new UpdateUserPassForm();
        form.setPass(member.getPass());
        form.setEditYourPassword("mingmark+"); //숫자가 없는 비밀번호 입력시
        form.setEditPasswordCheck("mingmark+");
        //when
        try {
            memberService.editPassword(member.getId(), form);
        } catch (Exception e) {
            return;
        }
        //then
        fail("에러가 발생하여야 됩니다.");
        assertThrows(IllegalStateException.class, () -> memberService.editPassword(member.getId(), form));
    }



    private Member createMember() {
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        return memberService.join(member);
    }

}