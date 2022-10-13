package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Comment;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.repository.CommentRepository;
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.ProductJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

import static project.toyproject.dto.CommentDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 댓글 작성
     * +대댓글 작성
     */
    @Transactional
    public Long addComment(CommentResponseDto form) {
        Product product = productJpaRepository.findById(form.getProductId()).orElseThrow(() -> new IllegalStateException("해당 상품이 없습니다."));
        Member member = memberJpaRepository.findById(form.getMemberId()).orElseThrow(() -> new IllegalStateException("해당 유저가 존재하지 않습니다"));
        Comment parentComment = null;
        if (form.getParentId() != null) {
            parentComment = commentRepository.findById(form.getParentId()).orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));
        }
        Comment comment = Comment.createComment(product, member, parentComment, form.getComment());
        Comment result = commentRepository.save(comment);
        return result.getId();
    }

    /**
     * 상품에 댓글,대댓글 출력
     */
    public List<CommentRequestDto> findByProductId(Long productId) {
        productJpaRepository.findById(productId).orElseThrow(() -> new IllegalStateException("해당 상품이 없습니다."));
        List<Comment> findComment = commentRepository.findByProductId(productId);
        return findComment.stream()
                .map(c -> new CommentRequestDto(c))
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    /**
     * 단일 댓글 출력
     */
    public CommentRequestDto findByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));
        return new CommentRequestDto(comment);
    }

    /**
     * 해당 부모 댓글의 자식 댓글 조회
     */
    public List<CommentRequestDto> findByParentId(Long commentId) {
        List<Comment> comments = commentRepository.findByParentId(commentId);
        return comments.stream()
                .map(c -> new CommentRequestDto(c))
                .collect(Collectors.toList());
    }

    /**
     * 대댓글만 삭제
     */
    @Transactional
    public void deleteChildComment(Long parentId) {
        if (parentId == null) {
            throw new IllegalStateException("해당 댓글이 없습니다.");
        }
        commentRepository.deleteByParentId(parentId);
    }

    /**
     * 부모 댓글과 대댓글 삭제
     */
    @Transactional
    public void deleteChildAndParent(Long commentId) {
        List<Comment> comments = commentRepository.findByParentId(commentId); //대댓글이 존재하는지 조회
        if (comments != null || comments.size() != 0) {  //대댓글이 존재할 경우
            commentRepository.deleteByParentId(commentId); //대댓글 삭제
        }
        commentRepository.deleteById(commentId);
    }


    /**
     * 상품에 있는 댓글들 삭제
     * @param productId
     */
    @Transactional
    public void deleteByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalStateException("해당 상품이 존재하지 않습니다.");
        }
        commentRepository.deleteByProductId(productId);
    }

}
