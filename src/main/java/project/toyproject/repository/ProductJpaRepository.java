package project.toyproject.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.toyproject.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    /**
     *  @EntityGraph(attributePaths = )
     *  JPA에서 fetch join을 어노테이션으로 사용할 수 있도록 만들어진 기능
     *  findAll()등 이미 JpaRepository안에 정의되어있는 기능은 오버라이딩을 통해 재정의하여 사용
     */
//    @EntityGraph(attributePaths = {"member"})
    List<Product> findByMemberId(Long memberId);

//    @Query("select p from Product p left join fetch p.member") //fetch join 쿼리 직접 작성하는 방법
    @EntityGraph(attributePaths = {"member"})
    Optional<Product> findById(Long productId);
}