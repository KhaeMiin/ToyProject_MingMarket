package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.MemberJpaRepository;

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
    
    @DisplayName("회원가입 실패")
    @Test
    void save_error() {
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

    private void createMember() {
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        Member result = memberService.join(member);
    }

}