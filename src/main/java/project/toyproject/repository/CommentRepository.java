package project.toyproject.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.toyproject.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"member"})
    List<Comment> findByProductId(Long productId);
}
