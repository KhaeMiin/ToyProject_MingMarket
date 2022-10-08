package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Comment;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.CommentDto;
import project.toyproject.dto.MemberDto;
import project.toyproject.repository.CommentRepository;
import project.toyproject.repository.ProductJpaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static project.toyproject.dto.CommentDto.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired ProductService productService;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("댓글 작성 성공")
    void addComment() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form = new CommentResponseDto(productId, member.getId(), null, "댓글입니다");
        //when
        Long result = commentService.addComment(form);
        //then
        Comment comment = commentRepository.findById(result).orElseThrow(() -> new IllegalStateException("댓글이 없습니다."));
        assertThat(result).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("댓글 작성 실패 1 - 없는 회원")
    void errorComment1() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form = new CommentResponseDto(productId, 9322L, null, "없는 회원인뎅");

        //when
        try {
            commentService.addComment(form);
        } catch (Exception e) {
            return;
        }
        //then
        fail();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> commentService.addComment(form));
        assertEquals("해당 유저가 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 작성 실패 2 - 없는 상품")
    void errorComment2() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form = new CommentResponseDto(329L, member.getId(), null, "없는 상품인댕");

        //when
        try {
            commentService.addComment(form);
        } catch (Exception e) {
            return;
        }
        //then
        fail();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> commentService.addComment(form));
        assertEquals("해당 상품이 없습니다.", exception.getMessage());
    }
    @Test
    @DisplayName("댓글 작성 실패 3 - 댓글")
    void errorComment3() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form = new CommentResponseDto(329L, member.getId(), 243L, "없는 대댓글인댕");

        //when
        try {
            commentService.addComment(form);
        } catch (Exception e) {
            return;
        }
        //then
        fail();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> commentService.addComment(form));
        assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("대댓글 작성 성공")
    void addComments() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form1 = new CommentResponseDto(productId, member.getId(), null, "댓글입니다");
        Long commentId = commentService.addComment(form1);
        CommentResponseDto form2 = new CommentResponseDto(productId, member.getId(), commentId, "대댓글입니다");
        Long result = commentService.addComment(form2);

        //when
        //then
        Comment comment = commentRepository.findById(result).orElseThrow(() -> new IllegalStateException("댓글이 없습니다."));
        assertThat(result).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("댓글과 대댓글 출력")
    void findByProductId() {
        //given
        Long productId = createComment();
        //when
        List<CommentRequestDto> findComments = commentService.findByProductIdComment(productId);
        //then
        assertThat(findComments.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
        //given
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form1 = new CommentResponseDto(productId, member.getId(), null, "댓글입니다");
        Long commentId = commentService.addComment(form1);
        CommentResponseDto form2 = new CommentResponseDto(productId, member.getId(), commentId, "대댓글입니다");
        Long parentCommentId = commentService.addComment(form2);

        //when
        commentService.deleteComment(commentId);

        //then
//        assertThat(commentRepository.findById(commentId)).isNull();
    }

    private Long createComment() {
        Member member = createMember();
        Long productId = createProduct(member);
        CommentResponseDto form1 = new CommentResponseDto(productId, member.getId(), null, "댓글입니다");
        Long commentId = commentService.addComment(form1);
        CommentResponseDto form2 = new CommentResponseDto(productId, member.getId(), commentId, "대댓글입니다");
        Long result = commentService.addComment(form2);
        return productId;
    }


    private Long createProduct(Member member) {
        Product product = Product.createProduct("test", "test.jpg", "test", 10000, member, CategoryList.BOOKS);
        return productService.saveProduct(member.getId(), product.getTitle(), product.getThumbnail(), product.getIntro(), product.getPrice(), product.getCategoryList());
    }

    private Member createMember() {
        MemberDto.CreateMemberForm member = new MemberDto.CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        return memberService.join(member);
    }
}