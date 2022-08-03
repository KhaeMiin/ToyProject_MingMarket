package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 댓글과 대댓글 > 정적인 쿼리가 아닌 동적 쿼리가 필요하다.
 * QUERY DSL 대한 지식이 많이 부족하다.
 * 일단 댓글기능은 추후에 다시 생각해보자.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id; //pk

    @ManyToOne(fetch = FetchType.LAZY) //하나의 상품에 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "product_id")
    private Product product; //해당 상품

    @ManyToOne(fetch = FetchType.LAZY) //회원 한명이 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "member_id")
    private Member member; //댓글 작성자

    private String content; //댓글내용
    private LocalDateTime writerTime; //작성날짜

    public Comment(Product product, Member member, String content) {
        this.product = product;
        this.member = member;
        this.content = content;
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    //생성 메서드
    public static Comment createComment(Product product, Member member, String content) {
        Comment comment = new Comment(product, member, content);
        comment.writerTime = LocalDateTime.now();
        return comment;
    }

    //수정 메서드
    public void change(String content) {
        this.content = content;
        this.writerTime = LocalDateTime.now();
    }
}
