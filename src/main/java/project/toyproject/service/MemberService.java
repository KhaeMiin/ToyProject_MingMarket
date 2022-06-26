package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.MemberRepository;

import java.util.List;

import static project.toyproject.dto.MemberDto.*;

@Service //Component > 자동 빈 등록(기본적인 내용이지만 적어둠)
@Transactional(readOnly = true) //스프링이 제공하는 트렌젝션
@RequiredArgsConstructor //final 붙은 필드만 가지고 생성자 만든다.
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        member.hashPassword(passwordEncoder); //스프링 시큐리티(암호화)
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 아이디 검증 메서드
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUserId(member.getUserId());
/*        if (!findMembers.isEmpty()) { //isEmpty(): 문자열 길이가 0일 경우 true 반환, 여기서는 !isEmpty: 값이 있다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }*/
        if (findMembers.size() > 0) { //이 코드가 더 최적화일 것 같다.
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAllMembers();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOneMember(Long memberId) {
        return memberRepository.findOneMember(memberId);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void editInformation(Long memberId, UpdateMemberForm form) {
        Member findMember = memberRepository.findOneMember(memberId);
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        findMember.change(form.getNickname(), form.getUsername(),form.getHp(), address);

    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public void editPassword(Long memberId, UpdateUserPassForm form) {
        Member findMember = memberRepository.findOneMember(memberId);
        findMember.passwordChange(form.getEditYourPassword());
    }
}
