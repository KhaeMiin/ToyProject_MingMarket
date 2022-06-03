package project.toyproject.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.toyproject.domain.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MemberDto {

    /**
     * 회원가입 DTO
     */
    @Getter @Setter
    public static class CreateMemberForm {

        @NotBlank(message = "회원아이디는 필수입니다.")
        @Size(min = 6, max = 15, message = "아이디는 6 ~ 15자 이여야 합니다!")
        private String userId;

        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(min = 1, max = 10, message = "닉네임은 1 ~ 10자 이여야 합니다!")
        private String nickname;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 20, message = "비밀번호는 10 ~ 20자 이여야 합니다!")
        private String pass;

        @NotBlank(message = "회원이름은 필수입니다.")
        private String username;

        @NotBlank(message = "연락처는 필수입니다.")
        private int hp;

        private String city;
        private String street;
        private String zipcode;
    }

    /**
     * 회원 정보 수정 DTO
     * 닉네임, 회원이름, 연락처, 주소만 수정 가능
     */
    @Getter @Setter
    public static class UpdateMemberForm {

        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(min = 1, max = 10, message = "닉네임은 1 ~ 10자 이여야 합니다!")
        private String nickname;

        @NotBlank(message = "회원이름은 필수입니다.")
        private String username;

        @NotBlank(message = "연락처는 필수입니다.")
        private int hp;

        private String city;
        private String street;
        private String zipcode;
    }

    /**
     * 비밀번호 수정 DTO
     * 비밀번호 변경 요청 사항 더 고민해보기
     */
    @Getter @Setter
    public static class UpdateUserPass {

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 20, message = "비밀번호는 10 ~ 20자 이여야 합니다!")
        private String pass;

    }


}
