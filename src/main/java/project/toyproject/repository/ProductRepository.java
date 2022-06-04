package project.toyproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.Product;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    // 상품 저장 or 상품 수정(merge 사용시 null값이 있을 경우 null로 저장될 위험이 있기때문에 merge 안쓰기!!!!
    public void save(Product product) {
        if (product.getId() == null) { // 등록된 상품이 없을 경우 새로 등록
            em.persist(product);
        } /*else { // 상품이 존재할 경우 강제로 업데이트(즉, 수정)
            em.merge(product);
        }*/
    }

    // 단일 상품 조회
    public Product findSingleProduct(Long productId) {
        return em.find(Product.class, productId);
    }

    // 전체 상품 조회
    public List<Product> findAllProducts() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    //상품 검색
    public List<Product> searchProduct(String productName) {
        return null;
    }
}
