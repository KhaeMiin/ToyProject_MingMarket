package project.toyproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;

import java.time.LocalDateTime;

public class WishItemDto {

    /**
     * 찜상품 조회하기
     */
    @Getter
    @AllArgsConstructor
    public static class FindWishItem {

        private long id;
        private Member member;
        private LocalDateTime wishDate;
        private Product product;
    }
}
