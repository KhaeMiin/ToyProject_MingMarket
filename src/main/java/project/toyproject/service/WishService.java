package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.*;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.WishRepository;
import project.toyproject.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 찜하기
     */
    @Transactional
    public Long addWishList(Long memberId, Long productId) {

        //엔티티 조회
        Member member = memberRepository.findOneMember(memberId);
        Product product = productRepository.findSingleProduct(productId);

        //찜상품 생성
        WishItem wish = WishItem.addWishItem(member, product);


        //찜상품 저장
        wishRepository.addWishList(wish);
        return wish.getId();
    }

    /**
     * 상품 찜 취소
     */
    @Transactional
    public void cancelWishItem(Long wishId) {
        wishRepository.cancel(wishId);
    }

    /**
     * 찜상품 조회 (단건)
     */
    public WishItem findOneOrder(Long wishId) {
        WishItem wishList = wishRepository.findOne(wishId);
        return wishList;
    }

    /**
     * 주문 조회 (회원 한명이 주문한 목록 리스트)
     */
    public List<WishItem> findMembers(String userId) {
        return wishRepository.findAll(userId);
    }

}
