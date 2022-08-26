package project.toyproject.test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
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
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.MemberRepository;
import project.toyproject.service.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static project.toyproject.dto.MemberDto.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberJpaRepository memberRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입_성공")
    void 회원가입_성공() {
        //given : 이런게 주어지면
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CreateMemberForm form = new CreateMemberForm();
        form.createMethod("testaaa1", "testNick", "testpass123*",
        "testpass123*", "test", 01011112222, "서울특별시 자바동", "밍마켓 1동 1호");
        String encodePass = encoder.encode(form.getPassword());
        Member member = new Member(form.getUserId(), form.getNickname(), encodePass,
                form.getUsername(), form.getHp(), new Address(form.getAddress(), form.getDetailedAddress()));
        given(memberRepository.save(any())).willReturn(member);//memberRepository.save(any())를 실행하면 member를 리턴


        //when : 실행하면
        Member joinMember = memberService.join(form);

        //then : 이렇게 된다(결과)
        //가입한 아이디와 방금 디비에 저장된 아이디 값 비교
//        System.out.println("userId = " + userId);
        assertThat(form.getUserId()).isEqualTo(joinMember.getUserId());

    }

    /**
     *
     */
    @Test
    @DisplayName("회원가입_실패")
    void 중복아이디_예외() {
        //given : 이런게 주어지면
        String userId = "usertest";


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CreateMemberForm form = new CreateMemberForm();
        form.createMethod(userId, "testNick", "testpass123*",
                "testpass123*", "test", 01011112222, "서울특별시 자바동", "밍마켓 1동 1호");
        String encodePass = encoder.encode(form.getPassword());
        Member member = new Member(form.getUserId(), form.getNickname(), encodePass,
                form.getUsername(), form.getHp(), new Address(form.getAddress(), form.getDetailedAddress()));

        doReturn(findByUserId()).when(memberRepository)
                .findByUserId(userId);

        given(memberRepository.save(any())).willReturn(member); //memberRepository.save(any())를 실행하면 member를 리턴

        //when : 실행하면
        try {
            memberService.join(form);
        } catch (Exception e) {
            return;
        }

        //then : 이렇게 된다(검증)
        fail("예외가 발생해야 합니다. (예외 발생 안함)"); //이 경우 테스트 실패
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(form));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }


    @Test
    void 전체회원_조회() {
        //given
        doReturn(memberList()).when(memberRepository)
                .findAll();

        //when
        List<SelectMemberData> members = memberService.findMembers();

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    private List<Member> memberList() {
        List<Member> memberList = new ArrayList<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePass = encoder.encode("password1223+");
        for (int i = 0; i < 3; i++) {
            memberList.add(new Member("mytest123" + i, "testNick", encodePass, "testName",
                    01022223333, new Address("서울특별시", "밍마켓21호")));
        }
        return memberList;
    }


    private List<Member> findByUserId() {
        List<Member> memberList = new ArrayList<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePass = encoder.encode("password1223+");
        memberList.add(new Member("usertest", "userNick", encodePass,
                "userName", 01011112222, new Address("서울특별시", "밍마켓21호")));
        return memberList;
    }
}