package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     * TODO
     * 코드 리팩토링 예정
     * 람다, stream 사용해보기
     * Optional 공부하기! get()말고 다른거 찾아보기
     */
    public Member login(String userId, String password) {
        Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

        //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
        if (!findMemberOptional.isPresent()) {
            return null;
        }

        Member member = findMemberOptional.get();

        if (member.getPass().equals(password)) { //비밀번호가 (일치) 있을 경우
            return member;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }
    }
}
