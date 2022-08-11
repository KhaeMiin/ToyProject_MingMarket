package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.*;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.WishItemRepository;
import project.toyproject.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemService {

    private final WishItemRepository wishItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 찜하기
     */
    @Transactional
    public Long addWishItem(Long memberId, Long productId) {

        //이미 찜한 상품인지 확인하기
        WishItem findWishItem = null;
        try {
            findWishItem = wishItemRepository.findOneItem(memberId, productId);
        } catch (Exception e) {
            e.getMessage();
        }
        if (findWishItem != null) { //이미 찜한 상품이면
            return null;
        }

        //엔티티 조회
        Member member = memberRepository.findOneMember(memberId);
        Product product = productRepository.findSingleProduct(productId);

        //찜상품 생성
        WishItem wish = WishItem.addWishItem(member, product);

        log.info("service에서={}", member.getId());
        log.info("service에서={}", product.getId());


        //찜상품 저장
        wishItemRepository.addWishList(wish);
        return wish.getId();
    }

    /**
     * 상품 찜 취소
     */
    @Transactional
    public void cancelWishItem(Long wishId) {
        wishItemRepository.cancel(wishId);
    }

    /**
     * 찜상품 조회(회원,상품으로 조회)
     */
    public Long findOneWishItem(Long memberId, Long productId) {
        WishItem wishItem = wishItemRepository.findOneItem(memberId, productId);

        return wishItem.getId();
    }

    /**
     * 내가 찜한 상품
     */
    public List<SelectProducts> wishList(Long memberId) {
        List<WishItem> wishItems = wishItemRepository.wishProduct(memberId);
        return wishItems.stream()
                .map(w -> new SelectProducts(w.getProduct().getId(), w.getProduct().getTitle(), w.getProduct().getThumbnail(),
                        w.getProduct().getIntro(), w.getProduct().getPrice(), w.getMember().getId(), w.getStatus()))
                .collect(Collectors.toList());

    }

}
