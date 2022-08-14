package project.toyproject.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static project.toyproject.dto.MemberDto.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입_성공")
    void 회원가입_성공() {
        //given : 이런게 주어지면
        CreateMemberForm form = new CreateMemberForm();
        form.createMethod("testaaa1", "testNick", "testpass123*",
        "testpass123*", "test", 01011112222, "서울특별시 자바동", "밍마켓 1동 1호");

        //when : 실행하면
        Long saveId = memberService.join(form);
        System.out.println("saveId = " + saveId);

        //then : 이렇게 된다(결과)
        //가입한 아이디와 방금 디비에 저장된 아이디 값 비교
//        System.out.println("userId = " + userId);
        Assertions.assertThat(form.getUserId()).isEqualTo(memberService.findOneMember(saveId).getUserId());

    }



}