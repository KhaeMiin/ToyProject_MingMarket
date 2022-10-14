package project.toyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.toyproject.domain.Product;
import project.toyproject.dto.ProductDto;

import java.util.List;
import java.util.Optional;

import static project.toyproject.dto.ProductDto.*;

public interface ProductRepositoryQuerydsl {

    List<SelectProducts> findAll();

    List<SelectProducts> findByMemberId(Long memberId);

    Page<SelectProducts> findProductsByMemberId(Long memberId, Pageable pageable);

    Optional<Product> findById(Long productId);

    Optional<Product> findProductById(Long productId);
}
