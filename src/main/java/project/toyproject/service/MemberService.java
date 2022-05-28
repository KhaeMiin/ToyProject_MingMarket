package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberRepository;

import java.util.List;

@Service //Component > 자동 빈 등록(기본적인 내용이지만 적어둠)
@Transactional(readOnly = true) //스프링이 제공하는 트렌젝션
@RequiredArgsConstructor //final 붙은 필드만 가지고 생성자 만든다.
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 아이디 검증 메서드
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
/*        if (!findMembers.isEmpty()) { //isEmpty(): 문자열 길이가 0일 경우 true 반환, 여기서는 !isEmpty: 값이 있다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }*/
        if (findMembers.size() > 0) { //이 코드가 더 최적화일 것 같다.
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAllMembers();
    }

    //회원 단건 조회
    public Member findOneMember(Long memberId) {
        return memberRepository.findOneMember(memberId);
    }
}
