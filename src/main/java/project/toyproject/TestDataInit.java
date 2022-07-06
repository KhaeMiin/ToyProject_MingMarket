package project.toyproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.toyproject.service.MemberService;
import project.toyproject.service.ProductService;

import javax.annotation.PostConstruct;

import static project.toyproject.dto.MemberDto.*;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final ProductService productService;

    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init() {
        CreateMemberForm member = new CreateMemberForm();
        member.createMethod("test", "min",
                "1234", "1234", "김성함", 0100000000, "서울시", "231-2");

        Long memberId = memberService.join(member);

        CreateMemberForm adminMember = new CreateMemberForm();
        adminMember.createMethod("admin", "admin",
                "1234", "1234", "관리자", 0100000000, "서울시 강남구 ", "밍마켓 123-3");

        Long adminMemberId = memberService.join(adminMember);

        for (int i = 1; i <= 13; i++) {
            productService.saveProduct(memberId, "test" + i, i + ".jpg", "test" + i, 10000);
            CreateMemberForm memberData = new CreateMemberForm();
            memberData.createMethod("test" + i, "user" + i,
                    "1234", "1234", "김성함" + i, 0100000000, "자바시 JPA구", "스프링");

            memberService.join(memberData);
        }
    }
}
