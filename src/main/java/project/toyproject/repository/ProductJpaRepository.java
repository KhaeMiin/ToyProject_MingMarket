package project.toyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.toyproject.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    /**
     *  @EntityGraph(attributePaths = )
     *  JPA에서 fetch join을 어노테이션으로 사용할 수 있도록 만들어진 기능
     *  findAll()등 이미 JpaRepository안에 정의되어있는 기능은 오버라이딩을 통해 재정의하여 사용
     */
    @Query("select p from Product p join fetch p.member") // 1. 쿼리 직접 작성
    @EntityGraph(attributePaths = {"member"}) //어노테이션 사용
    @Override
    List<Product> findAll();

    @Query("select p from Product p join fetch p.member") //쿼리 직접 작성
    @EntityGraph(attributePaths = {"member"}) //어노테이션 사용
    List<Product> findByMemberId(Long memberId);

    //페이징
    @EntityGraph(attributePaths = {"member"}) //어노테이션 사용
    Page<Product> findProductsByMemberId(Long memberId, Pageable pageable);

//    @Query("select p from Product p left join fetch p.member") //fetch join 쿼리 직접 작성하는 방법
    @EntityGraph(attributePaths = {"member"})
    Optional<Product> findById(Long productId);
}
