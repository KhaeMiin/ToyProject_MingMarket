package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity { //상품

    @Id @GeneratedValue()
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

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    /**
     * CASCADE는 JPA 영속성 전이
     * 부모 엔티티가 영속화될 때 자식 엔티티도 같이 영속화되고, 부모 엔티티가 삭제될 때 자식 엔티티도
     * 삭제되는 등 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 전이되는 것을 의미
     * orphanRemoval = true
     * 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제해주는 기능
     * CascadeType.ALL + orphanRemoval = true
     * 이 두개를 같이 사용하게 되면 부모 엔티티가 자식의 생명주기를 모두 관리할 수 있게 된다.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)  //누구에 의해서 맵핑 되는지!(읽기전용이 됩니다!)
    private List<Comment> commentList = new ArrayList<>();

    /**
     * 무한카테고리(현재는 구현 안 할 예정) > 나중에 다시 구현하자
     */
/*    @OneToOne(fetch = LAZY) @JoinColumn(name = "category_id")
    private Category category; //fk*/

    private Product(String title, String thumbnail, String intro, int price, Member member, ProductStatus productStatus, CategoryList categoryList) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.intro = intro;
        this.Price = price;
        this.member = member;
        this.productStatus = productStatus;
        this.categoryList = categoryList;
    }

    //연관관계 메서드
    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.addProduct(this);
    }

    //생성 메서드
    public static Product createProduct(String title, String thumbnail, String intro, int price, Member member, CategoryList categoryList) {
        Product product = new Product(title, thumbnail, intro, price, member, ProductStatus.PRODUCT, categoryList);
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
