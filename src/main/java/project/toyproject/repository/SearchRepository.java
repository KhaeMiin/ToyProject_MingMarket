package project.toyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Product;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContaining(@Param("keyword") String keyword);

    //페이징 처리
    Page<Product> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
}
