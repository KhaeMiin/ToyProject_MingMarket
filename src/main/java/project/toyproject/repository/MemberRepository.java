package project.toyproject.repository;

import org.springframework.stereotype.Repository;
import project.toyproject.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext //스프링 제공
    private EntityManager em;

    // 회원 저장
    public void save(Member member) {
        em.persist(member);
    }

    //회원 단건 조회(관리자)
    public Member findOneMember(Long id) {
        return em.find(Member.class, id);
    }

    //회원 전체 조회(관리자)
    public List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //회원 아이디로 검색하기(관리자)
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }
}
