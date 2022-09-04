package project.toyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.toyproject.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    List<Member> findMemberByUserId(String userId);

    Page<Member> findALl(Pageable pageable);

    Optional<Member> findByUserId(String userId);
}
