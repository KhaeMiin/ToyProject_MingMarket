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
     * 찜상품 조회 (단건)
     */
    public WishItem findOneOrder(Long wishId) {
        WishItem wishList = wishItemRepository.findOne(wishId);
        return wishList;
    }

    /**
     * 찜상품 조회(회원,상품)
     */
    public WishItem findOneWishItem(Long memberId, Long productId) {
        WishItem wishItem = wishItemRepository.findOneItem(memberId, productId);
        return wishItem;
    }

    /**
     * 주문 조회 (회원 한명이 주문한 목록 리스트)
     */
    public List<WishItem> findMembers(String userId) {
        return wishItemRepository.findAll(userId);
    }

}
