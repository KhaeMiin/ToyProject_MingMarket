package project.toyproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Product;
import project.toyproject.domain.WishItem;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    /**
     * 상품 저장
     */
    public void save(Product product) {
        if (product.getId() == null) { // 등록된 상품이 없을 경우 새로 등록
            em.persist(product);
        } /*else { // 상품이 존재할 경우 강제로 업데이트(즉, 수정)
            em.merge(product);
        }*/
    }

    /**
     * 단일 상품 조회
     * TODO
     * 댓글 기능 추가후 단일 상품 정보 조회시 fetch join으로 댓글 정보 가져오기
     */
    public Product findSingleProduct(Long productId) {
        return em.find(Product.class, productId);
    }

    /**
     * 전체 상품 조회
     */
    public List<Product> findAllProducts() {
        return em.createQuery("select p from Product p order by p.id desc ", Product.class)
                .getResultList();
    }

    /**
     * 상품 삭제
     */
    public void removeProduct(Long productId) {
        Product singleProduct = findSingleProduct(productId);
        em.remove(singleProduct);
    }

    /**
     * 내가 올린 상품 리스트 보기
     */
    public List<Product> userProducts(Long memberId) {
        return em.createQuery("select p from Product p where p.member.id = :memberId", Product.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
