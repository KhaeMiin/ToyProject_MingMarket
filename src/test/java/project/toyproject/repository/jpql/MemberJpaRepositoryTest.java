package project.toyproject.repository.jpql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.repository.jpql.MemberJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // 실제 구성된 db 사용
class MemberJpaRepositoryTest {

    @Autowired private MemberJpaRepository memberJpaRepository;

    @DisplayName("회원 저장")
    @Test
    void save() {
        Member member = createMember();
        Member saveMember = memberJpaRepository.save(member);

        assertThat(member).isEqualTo(saveMember);
    }

    @DisplayName("회원 단건 조회")
    @Test
    void findById() {
        Member member = createMember();
        memberJpaRepository.save(member);

        Member findOneMember = memberJpaRepository.findById(member.getId()).get();

        assertThat(findOneMember).isEqualTo(member);

    }

    @DisplayName("회원 전체조회")
    @Test
    void findAll() {
        Member member1 = createMember("AAA1", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
        Member member2 = createMember("AAA2", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
        Member member3 = createMember("AAA3", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
    }

/*    @DisplayName("회원이름으로 검색")
    @Test
    void findMemberByUserId() {
        Member member1 = createMember();
        memberJpaRepository.save(member1);

        List<Member> memberByUserId = memberJpaRepository.findMemberByUserId(member1.getUserId());

        assertThat(memberByUserId.get(0).getUserId()).isEqualTo("AAA1");

    }*/

    @DisplayName("로그인시 회원 조회 (값 있음)")
    @Test
    void findByLoginId() {
        Member member1 = createMember();
        memberJpaRepository.save(member1);

        Optional<Member> result = memberJpaRepository.findByUserId(member1.getUserId());
        Member member = result.get();
        assertThat(member.getUserId()).isEqualTo(member1.getUserId());

    }

    private static Member createMember() {
        return new Member("AAA1", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
    }

    private Member createMember(String userId, String nickname, String pass, String username, int hp, Address address) {
        return new Member(userId, nickname, pass, username, hp, address);
    }

}