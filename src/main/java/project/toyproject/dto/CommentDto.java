package project.toyproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentDto {

    /**
     * 댓글 등록
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommentResponseDto {
        private Long productId; //댓글 다는 게시글

        private Long memberId; //댓글 작성자

        private Long parentId; //대댓글일 경우 상위 댓글 pk (대댓글 아닐 경우 null)

        private String comment; //댓글 내용

    }

    /**
     * 댓글 출력
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommentRequestDto {
    }
}
