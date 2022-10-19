package project.toyproject.repository.jpql;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toyproject.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    List<Member> findMemberByUserId(String userId); //아이디 검색

    Optional<Member> findByUserId(String userId);
}
