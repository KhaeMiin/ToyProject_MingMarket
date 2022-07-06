package project.toyproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.toyproject.domain.WishItem;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishItemRepository {

    private final EntityManager em;

    /**
     * 상품 찜하기
     */
    public void addWishList(WishItem wishItem) {
        em.persist(wishItem);
    }

    /**
     * 상품 조회(단건)
     */
    public WishItem findOne(Long id) {
        return em.find(WishItem.class, id);
    }

    /**
     * 찜상품 조회(회원, 상품)
     */
    public WishItem findOneItem(Long memberId, Long productId) {
        return em.createQuery("select w from WishItem w where w.member.id = :userId and w.product.id = :productId", WishItem.class)
                .setParameter("userId", memberId)
                .setParameter("productId", productId)
                .getSingleResult();
    }

    /**
     * TODO
     * 찜 상품 전체 리스트
     */
    public List<WishItem> findAll(String userId) {
        return em.createQuery("select w from WishItem w where w.member.userId = :userId", WishItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 찜 상품 취소
     */
    public void cancel(Long wishId) {
        WishItem findWishItem = findOne(wishId);
        em.remove(findWishItem);
    }

}