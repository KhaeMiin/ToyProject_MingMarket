package project.toyproject.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.toyproject.domain.Product;
import project.toyproject.domain.QMember;
import project.toyproject.domain.QProduct;
import project.toyproject.dto.ProductDto;
import project.toyproject.dto.QProductDto_SelectProducts;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static project.toyproject.domain.QMember.*;
import static project.toyproject.domain.QProduct.*;

public class ProductRepositoryImpl implements ProductRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * TODO
     * querydsl 코드 전체 테스트 전
     * n + 1 잘 확인하면서 테스트해보기
     * @return
     */

    @Override
    public List<ProductDto.SelectProducts> findAll() {
        return queryFactory
                .select(new QProductDto_SelectProducts(product))
                .from(product)
                .leftJoin(product.member, member)
                .fetch();
    }

    @Override
    public List<ProductDto.SelectProducts> findByMemberId(Long memberId) {
        return queryFactory
                .select(new QProductDto_SelectProducts(product))
                .from(product)
                .where(
                        memberIdEq(memberId)
                )
                .fetch();
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

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? product.member.id.eq(memberId) : null; //값이 있으면 비교해서 값 반환, 없으면 null반환
    }
}
