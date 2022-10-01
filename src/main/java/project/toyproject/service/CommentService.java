package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Comment;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.CommentDto;
import project.toyproject.repository.CommentRepository;
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.ProductJpaRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public Long addComment(CommentDto.CommentResponseDto form) {
        Product product = productJpaRepository.findById(form.getProductId()).orElseThrow(() -> new IllegalStateException("해당 상품이 없습니다."));
        Member member = memberJpaRepository.findById(form.getMemberId()).orElseThrow(() -> new IllegalStateException("해당 유저가 존재하지 않습니다"));
        Comment parentComment = commentRepository.findById(form.getParentId()).orElseThrow(() -> new IllegalStateException("해당 댓글이 존재하지 않습니다."));
        Comment comment = Comment.createComment(product, member, parentComment, form.getComment());
        Comment result = commentRepository.save(comment);
        return result.getId();
    }

}
