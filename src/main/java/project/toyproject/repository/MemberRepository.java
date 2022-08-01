package project.toyproject.repository;

import org.springframework.stereotype.Repository;
import project.toyproject.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext //스프링 제공
    private EntityManager em;

    // 회원 저장
    public void save(Member member) {
        em.persist(member);
    }

    //회원 단건 조회(관리자)
    public Member findOneMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    //회원 전체 조회(관리자)
    public List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //회원 아이디로 검색하기(관리자)
    public List<Member> findByUserId(String userId) {
        return em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 로그인시 회원 조회
     * TODO
     */
    public Optional<Member> findByLoginId(String userId) {
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
/*
        for (Member m : members) {
            if (m.getUserId().equals(userId)) { //값이 있을 경우
                return Optional.of(m);
            }
        }
        return Optional.empty(); //값이 없으면 null
*/

        //stream 사용
        //findFirst(): filter 조건에 일치하는 element 1개를 Optional로 리턴
        //만약 조건이 일치하는 값이 없으면 empty 리턴
        //+비슷한 기능으로 findAny()도 있지만 순서 상관없이 먼저 찾는 요소를 리턴해버림
        //empty: 비어있는(즉, 값이 없는)
        return members.stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst();

    }

}
