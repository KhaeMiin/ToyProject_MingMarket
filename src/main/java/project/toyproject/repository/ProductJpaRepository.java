package project.toyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toyproject.domain.Product;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findProductByMemberId(Long memberId);
}
