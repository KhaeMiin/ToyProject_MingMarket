package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.dto.LoginDto;
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.MemberRepository;

import java.util.Optional;

import static project.toyproject.dto.MemberDto.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService{

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     */
    public SessionMemberData login(LoginDto form) {
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(form.getUserId()); // 순수JPA
        Optional<Member> findMember = memberJpaRepository.findByUserId(form.getUserId()); //Spring Data JPA

        //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
        if (!findMember.isPresent()) { // boolean isPresent()
            return null;
        }


        Member member = findMember.get();

        /**
         * 비밀번호 확인 (스프링 시큐리티)
         * password 암호화 이전의 비밀번호
         * member.getPass() 암호화에 사용된 클래스
         * @return true/ false
         */
        if (passwordEncoder.matches(form.getPassword(), member.getPass())) {
            Address address = member.getAddress();
            SessionMemberData memberData = new SessionMemberData(member);
            return memberData;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }

/*        if (member.getPass().equals(password)) { //비밀번호가 (일치) 있을 경우
            return member;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }*/
    }

    /**
     * 비밀번호 체크 (비밀번호 수정시 사용)
     */
    public Long passwordCheck(Long memberId, String password) {
//        Member member = memberRepository.findOneMember(Long.valueOf(memberId)); // 순수JPA
        Member member = memberJpaRepository.findById(memberId).orElseThrow(IllegalStateException::new); //Spring Data JPA

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