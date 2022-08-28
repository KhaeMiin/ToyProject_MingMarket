package project.toyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.toyproject.domain.WishItem;

import java.util.List;
import java.util.Optional;

public interface WishItemJpaRepository extends JpaRepository<WishItem, Long> {

    //회원,상품 ID로 찜상품 조회하기
    @Query("select w from WishItem w where w.member.id = :userId and w.product.id = :productId")
    Optional<WishItem> findOneItem(@Param("userId") Long userId, @Param("productId") Long productId);

    List<WishItem> findByMemberId(Long memberId); //내가 찜한 리스트
}

