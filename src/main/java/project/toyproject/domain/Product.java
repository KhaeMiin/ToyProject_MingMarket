package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity { //상품

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id; //pk

    private String title; //제목
    private String thumbnail; //섬네일
    private String intro; //설명(게시판)
    private int Price; //상품가격

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //fk

    @Enumerated(EnumType.STRING)
    private CategoryList categoryList;

    /**
     * 무한카테고리(현재는 구현 안 할 예정) > 나중에 다시 구현하자
     */
/*    @OneToOne(fetch = LAZY) @JoinColumn(name = "category_id")
    private Category category; //fk*/

    public Product(String title, String thumbnail, String intro, int price, Member member) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.intro = intro;
        this.Price = price;
        this.member = member;
    }

    //생성 메서드
    public static Product createProduct(String title, String thumbnail, String intro, int price, Member member) {
        Product product = new Product(title, thumbnail, intro, price, member);
        product.createDate(LocalDateTime.now());
        return product;
    }
    //수정 메서드
    public void change(String title, String thumbnail, String intro, int price) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.intro = intro;
        this.Price = price;
    }

}
