package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "WishItems")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishItem {

    @Id @GeneratedValue
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //어떤 관계인지
    @JoinColumn(name = "member_id") //Join할 컬럼 명
    private Member member; //FK (현재 클래스가 연관관계 주인) : 회원 한명은 여러개의 상품을 찜할 수 있다.
    private LocalDateTime wishDate;

    @Enumerated(EnumType.STRING)
    private WishItemStatus status; //현재 주문 상태

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product; //FK 찜 한번에 하나의 상품을 가질 수 있다.

    public WishItem(Member member, LocalDateTime wishDate, WishItemStatus status, Product product) {
        this.member = member;
        this.wishDate = wishDate;
        this.status = status;
        this.product = product;
    }

    //==생성 메서드==//
    public static WishItem addWishItem(Member member, Product product) {
        WishItem wishItem = new WishItem(member, LocalDateTime.now(), WishItemStatus.WISH, product); //생성시(찜하기) 상태: WISH 으로 초기화
        return wishItem;
    }
}
