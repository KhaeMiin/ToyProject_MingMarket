package project.toyproject.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 댓글과 대댓글 > 정적인 쿼리가 아닌 동적 쿼리가 필요하다.
 * QUERY DSL 대한 지식이 많이 부족하다.
 * 일단 댓글기능은 추후에 다시 생각해보자.
 */
//@Entity
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne //하나의 상품에 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "item_id")
    private Product item;

    @ManyToOne //회원 한명이 여러 댓글을 남길 수 있다.
    @JoinColumn(name = "member_id")
    private Member member; //댓글 작성자

    private String content; //댓글내용
    private LocalDateTime writerTime;

}
