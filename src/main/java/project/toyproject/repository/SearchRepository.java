package project.toyproject.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Product;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContaining(String keyword);
}
