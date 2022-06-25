package project.toyproject.dto;

import lombok.*;
import project.toyproject.domain.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
        private String password;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 20, message = "비밀번호는 10 ~ 20자 이여야 합니다!")
        private String passwordCheck;

        @NotBlank(message = "회원이름은 필수입니다.")
        private String username;

        @NotNull(message = "연락처는 필수입니다.")
        private int hp;

        private String address;
        private String detailedAddress;
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

        @NotNull(message = "연락처는 필수입니다.")
        private int hp;

        @NotBlank(message = "주소를 입력해주세요")
        private String address;
        @NotBlank(message = "상세주소를 입력해주세요")
        private String detailedAddress;

        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        private String pass;

        public UpdateMemberForm(String nickname, String username, int hp, String address, String detailedAddress) {
            this.nickname = nickname;
            this.username = username;
            this.hp = hp;
            this.address = address;
            this.detailedAddress = detailedAddress;
        }
    }

    /**
     * 비밀번호 수정 DTO
     * 비밀번호 변경 요청 사항 더 고민해보기
     */
    @Getter @Setter
    public static class UpdateUserPassForm {

        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        @Size(min = 10, max = 20, message = "잘못된 입력입니다.")
        private String pass;

        // == 수정 비밀번호 ==//
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 20, message = "비밀번호는 10 ~ 20자 이여야 합니다!")
        private String editYourPassword;
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 20, message = "비밀번호는 10 ~ 20자 이여야 합니다!")
        private String editPasswordCheck;

    }

    /**
     * Session에 담을 회원 정보
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SessionMemberData {

        private Long memberId; //pk

        private String userId; // 아이디

        private String nickname;

        private String username;

    }

    /**
     * 회원정보 출력
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SelectMemberData {
        private Long memberId;
        private String userId;
        private String username;
        private int hp;
        private Address address;
    }

}
