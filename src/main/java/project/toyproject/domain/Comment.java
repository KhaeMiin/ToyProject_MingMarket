package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

/**
 * 댓글과 대댓글 > 정적인 쿼리가 아닌 동적 쿼리가 필요하다.
 * QUERY DSL 대한 지식이 많이 부족하다.
 * 일단 댓글기능은 추후에 다시 생각해보자.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue()
    @Column(name = "comment_id")
    private Long id; //pk

    @ManyToOne(fetch = LAZY) //하나의 상품에 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "product_id")
    private Product product; //해당 상품

    @ManyToOne(fetch = LAZY) //회원 한명이 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "member_id")
    private Member member; //댓글 작성자

    private String comment; //댓글내용
    private LocalDateTime writerTime; //작성날짜

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)//댓글이 삭제되면 대댓글 모두 삭제
    private List<Comment> children = new ArrayList<>(); //대댓글 List

    public Comment(Product product, Member member, Comment parent, String comment) {
        this.product = product;
        this.member = member;
        this.comment = comment;
        this.parent = parent;
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    //생성 메서드
    public static Comment createComment(Product product, Member member, Comment parent, String comment) {
        Comment cm = new Comment(product, member, parent, comment);
        cm.writerTime = LocalDateTime.now();
        return cm;
    }

    //수정 메서드
    public void change(String comment) {
        this.comment = comment;
        this.writerTime = LocalDateTime.now();
    }
}
