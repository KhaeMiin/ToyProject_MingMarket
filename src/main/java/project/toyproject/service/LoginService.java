package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberRepository;

import java.util.Optional;

import static project.toyproject.dto.MemberDto.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 로그인
     */
    public SessionMemberData login(String userId, String password) {
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(userId);

        //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
        if (!findMemberOptional.isPresent()) {
            return null;
        }

        Member member = findMemberOptional.get();

        /**
         * 비밀번호 확인 (스프링 시큐리티)
         * password 암호화 이전의 비밀번호
         * member.getPass() 암호화에 사용된 클래스
         * @return true/ false
         */
        if (passwordEncoder.matches(password, member.getPass())) {
            Address address = member.getAddress();
            SessionMemberData memberData = new SessionMemberData(
                    member.getId(), member.getUserId(), member.getNickname(), member.getUsername());
            return memberData;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }
    }

    /**
     * 비밀번호 체크 (비밀번호 수정시 사용)
     */
    public Long passwordCheck(Long memberId, String password) {
        Member member = memberRepository.findOneMember(Long.valueOf(memberId));

        /**
         * 비밀번호 확인 (스프링 시큐리티)
         * password 암호화 이전의 비밀번호
         * member.getPass() 암호화에 사용된 클래스
         * @return passwordEncoder.matches = true/ false
         */
        if (passwordEncoder.matches(password, member.getPass())) {
            return member.getId();
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }
    }


}
