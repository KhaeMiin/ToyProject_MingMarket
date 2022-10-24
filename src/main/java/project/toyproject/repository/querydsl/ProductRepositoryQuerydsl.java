package project.toyproject.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.toyproject.domain.Product;

import java.util.List;
import java.util.Optional;

import static project.toyproject.dto.ProductDto.*;

public interface ProductRepositoryQuerydsl {

    List<SelectProducts> findAllProduct();

    List<SelectProducts> findByMemberId(Long memberId);

    Page<SelectProducts> findProductsByMemberId(Long memberId, Pageable pageable);

}
