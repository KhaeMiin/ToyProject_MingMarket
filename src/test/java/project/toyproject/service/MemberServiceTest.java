package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    void 회원가입() throws Exception {
        //given :이런게 주어지면
        Address address = new Address("city", "street");
        Member member = new Member("test", "min", "1234", "해민", 01000000000, address);

        //when : 이렇게 하면(실행)
        Long saveId = memberService.join(member);

        //then : 이렇게 된다(검증)
        Assertions.assertThat(member).isEqualTo(memberService.findOneMember(saveId));
    }

    @Test
    void 중복_아이디_예외() throws Exception {
        //given :이런게 주어지면
        Address address = new Address("city", "street");
        Member member1 = new Member("test", "min", "1234", "해민", 01000000000, address);

        Address address2 = new Address("city", "street");
        Member member2 = new Member("test", "min", "1234", "해민", 01000000000, address2);

        //when : 이렇게 하면(실행)
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }

        //then : 이렇게 된다(검증)
        fail("예외가 발생해야 합니다. (예외 발생 안함)"); //이 경우 테스트 실패
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }

}