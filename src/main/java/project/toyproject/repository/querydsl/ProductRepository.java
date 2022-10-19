package project.toyproject.repository.querydsl;

import org.springframework.data.jpa.repository.JpaRepository;
import project.toyproject.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuerydsl {

}
