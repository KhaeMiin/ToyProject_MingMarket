package project.toyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toyproject.domain.Member;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    List<Member> findMemberByUserId(String userId);
}
