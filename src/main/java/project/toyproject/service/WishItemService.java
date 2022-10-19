package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.*;
import project.toyproject.repository.jpql.MemberJpaRepository;
import project.toyproject.repository.jpql.ProductJpaRepository;
import project.toyproject.repository.jpql.WishItemJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemService {

    private final WishItemJpaRepository wishItemJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ProductJpaRepository productJpaRepository;


    /**
     * 상품 찜하기
     */
    @Transactional
    public Long addWishItem(Long memberId, Long productId) {

        /**
         * 이 부분을 왜 try/catch 처리를 하였는가? NoResultException 발생함
         * Query.getSingleResult() 호출 시 결과가 하나도 없을 때 발생
         */
        //이미 찜한 상품인지 확인하기
/*        WishItem findWishItem = null;
        try {
            findWishItem = wishItemRepository.findOneItem(memberId, productId);
        } catch (Exception e) {
            e.getMessage();
        }*/

        Optional<WishItem> findWishItem = wishItemJpaRepository.findOneItem(memberId, productId);


        if (!findWishItem.isEmpty()) { //이미 찜한 상품이면
            return 0L;
        }

        //엔티티 조회
//        Member member = memberRepository.findOneMember(memberId); //순수 JPA
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> { //Spring Data JPA
            throw new IllegalArgumentException("조회된 정보가 없습니다.");
        });

//        Product product = productRepository.findSingleProduct(productId); //순수 JPA
        Product product = productJpaRepository.findById(productId).orElseThrow(() -> { //Spring Data JPA
            throw new IllegalArgumentException("조회된 정보가 없습니다.");
        });

        //찜상품 생성
        WishItem wish = WishItem.addWishItem(member, product);

        //찜상품 저장
//        wishItemRepository.addWishList(wish); // 순수 JPA
        wishItemJpaRepository.save(wish); //Spring Data JPA
        return wish.getId();
    }

    /**
     * 상품 찜 취소
     */
    @Transactional
    public void cancelWishItem(Long wishId) {
//        wishItemRepository.cancel(wishId); // 순수 JPA
        wishItemJpaRepository.deleteById(wishId); // Spring Data JPA
    }

    /**
     * 찜상품 조회(회원,상품으로 조회)
     */
    public Long findOneWishItem(Long memberId, Long productId) {
//        WishItem wishItem = wishItemRepository.findOneItem(memberId, productId); // 순수 JPA
        WishItem wishItem = wishItemJpaRepository.findOneItem(memberId, productId).orElseThrow(() -> { //Spring Data JPA
            throw new IllegalArgumentException("상품 정보가 없습니다.");
        });
        return wishItem.getId();
    }

    /**
     * 내가 찜한 상품 (fetch join 상태)
     */
    public List<SelectProducts> wishList(Long memberId) {
//        List<WishItem> wishItems = wishItemRepository.wishProduct(memberId); // 순수 JPA
        List<WishItem> wishItems = wishItemJpaRepository.findByMemberId(memberId); //Spring Data JPA
        return wishItems.stream()
                .map(w -> new SelectProducts(w.getProduct()))
                .collect(Collectors.toList());
    }

    /**
     * 내가 찜한 상품 (페이징 처리)
     */
    public Page<SelectProducts> wishListPage(Long memberId, Pageable pageable) {
        Page<WishItem> wishItems = wishItemJpaRepository.findByMemberId(memberId, pageable);//Spring Data JPA
        return wishItems.map(w -> new SelectProducts(w.getProduct()));
    }

}
