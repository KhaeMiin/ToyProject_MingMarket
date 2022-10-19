package project.toyproject.repository.jpql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.toyproject.domain.*;
import project.toyproject.repository.jpql.CommentRepository;
import project.toyproject.repository.jpql.MemberJpaRepository;
import project.toyproject.repository.jpql.ProductJpaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired private ProductJpaRepository productJpaRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("댓글 작성")
    @Test
    void save() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Product saveProduct = productJpaRepository.save(product);
        Comment comment = Comment.createComment(product, member, null, "댓글입니다~");
        //when
        Comment result = commentRepository.save(comment);
        //then
        assertThat(result.getComment()).isEqualTo(comment.getComment());
        assertThat(result.getProduct()).isEqualTo(comment.getProduct());
        assertThat(result.getMember()).isEqualTo(comment.getMember());
    }

    @DisplayName("대댓글 작성")
    @Test
    void save2() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Member member2 = createMember();
        Product saveProduct = productJpaRepository.save(product);
        Comment parentComment = Comment.createComment(product, member, null, "댓글입니다~");
        Comment saveParentComment = commentRepository.save(parentComment);
        Comment childComment = Comment.createComment(product, member2, saveParentComment, "대댓글 입니다~");
        //when
        Comment result = commentRepository.save(childComment);
        //then
        assertThat(result.getParent()).isEqualTo(saveParentComment);
        assertThat(result.getComment()).isEqualTo(childComment.getComment());
        assertThat(result.getProduct()).isEqualTo(childComment.getProduct());
        assertThat(result.getMember()).isEqualTo(childComment.getMember());
    }

    @DisplayName("내가 작성한 댓글 리스트 조회")
    @Test
    void findById() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Member resultMember = memberJpaRepository.save(member);
        Comment comment1 = addComment(product, member, "1댓글입니다~");
        Comment comment2 = addComment(product, member, "2댓글입니다~");
        Comment comment3 = addComment(product, member, "3댓글입니다~");
        Comment childComment = addChildComment(comment1, member, "댓글1의 대댓글입니다~");

        //when
        List<Comment> result = commentRepository.findByMemberId(resultMember.getId());

        //then
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0)).isEqualTo(comment1);
        assertThat(childComment.getParent()).isEqualTo(comment1);
    }

    @DisplayName("상품에 달린 댓글 리스트 조회")
    @Test
    void findByProductId() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Member resultMember = memberJpaRepository.save(member);
        Comment comment1 = addComment(product, member, "1댓글입니다~");
        Comment comment2 = addComment(product, member, "2댓글입니다~");
        Comment comment3 = addComment(product, member, "3댓글입니다~");
        Comment childComment = addChildComment(comment1, member, "댓글1의 대댓글입니다~");
        //when
        List<Comment> result = commentRepository.findByProductId(product.getId());
        //then
        assertThat(result.size()).isEqualTo(4);
    }

    @DisplayName("단일 댓글 삭제")
    @Test
    void deleteId() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Member resultMember = memberJpaRepository.save(member);
        Comment comment1 = addComment(product, member, "1댓글입니다~");
        Comment comment2 = addComment(product, member, "2댓글입니다~");
        Comment comment3 = addComment(product, member, "3댓글입니다~");
        Comment childComment = addChildComment(comment1, member, "댓글1의 대댓글입니다~");
        //when
        commentRepository.deleteById(comment1.getId());
        //then
        assertThat(commentRepository.findById(comment1.getId())).isEmpty();
    }

    @DisplayName("상품에 댓글 삭제")
    @Test
    void deleteByProductId() {
        //given
        Product product = createProduct();
        Member member = createMember();
        Member resultMember = memberJpaRepository.save(member);
        Comment comment1 = addComment(product, member, "1댓글입니다~");
        Comment comment2 = addComment(product, member, "2댓글입니다~");
        Comment comment3 = addComment(product, member, "3댓글입니다~");
        Comment childComment = addChildComment(comment1, member, "댓글1의 대댓글입니다~");
        //when
        commentRepository.deleteByProductId(product.getId());
        //then
        List<Comment> results = commentRepository.findByProductId(product.getId());
        assertThat(results.size()).isEqualTo(0);
    }

    //댓글
    private Comment addComment(Product product, Member member, String comment) {
        Product saveProduct = productJpaRepository.save(product);
        Comment result = Comment.createComment(product, member, null, comment);
        return commentRepository.save(result);
    }

    //대댓글
    private Comment addChildComment(Comment parentComment, Member member, String comment) {
        Comment result = Comment.createComment(parentComment.getProduct(), member, parentComment, comment);
        return commentRepository.save(result);
    }


    public static Product createProduct() {
        Member member = createMember();
        return Product.createProduct("test", "tt.jpg", "test", 20000, member, CategoryList.BOOKS);
    }


    private static Member createMember() {
        return new Member("AAA1", "test", "test", "test", 01022223333, new Address("ss", "123-4"));
    }
}