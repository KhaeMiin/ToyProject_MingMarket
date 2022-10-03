package project.toyproject.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentDto {

    /**
     * 댓글 등록
     */
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CommentResponseDto {
        @NotBlank(message = "해당 상품이 없습니다.")
        private Long productId; //댓글 다는 게시글

        @NotBlank(message = "해당 회원이 없습니다.")
        private Long memberId; //댓글 작성자

        private Long parentId; //대댓글일 경우 상위 댓글 pk (대댓글 아닐 경우 null)

        @NotBlank(message = "내용을 입력해주세요(공백 제외)")
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
