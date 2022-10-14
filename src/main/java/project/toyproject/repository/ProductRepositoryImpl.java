package project.toyproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.toyproject.domain.Product;
import project.toyproject.domain.QProduct;
import project.toyproject.dto.ProductDto;
import project.toyproject.dto.QProductDto_SelectProducts;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static project.toyproject.domain.QProduct.*;

public class ProductRepositoryImpl implements ProductRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<ProductDto.SelectProducts> findAll() {
        return queryFactory
                .select(new QProductDto_SelectProducts(product))
                .from(product)
                .fetch();
    }

    @Override
    public List<ProductDto.SelectProducts> findByMemberId(Long memberId) {
        return null;
    }

    @Override
    public Page<ProductDto.SelectProducts> findProductsByMemberId(Long memberId, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return Optional.empty();
    }

    @Override
    public Optional<Product> findProductById(Long productId) {
        return Optional.empty();
    }
}
