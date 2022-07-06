package project.toyproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
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
                "1234", "1234", "김성함", 0100000000, "머라구", "어쩌라는동");

        Long memberId = memberService.join(member);




        Address address = new Address("자바시 JPA구", "스프링");
        for (int i = 1; i < 16; i++) {
            productService.saveProduct(memberId, "test" + i, i + ".jpg", "test" + i, 10000);
            CreateMemberForm memberData = new CreateMemberForm();
/*            member.createMethod("test" + i, "user" + i,
                    "1234", "1234", "김성함" + i, 0100000000, "머라구", "어쩌라는동");

            memberService.join(memberData);*/
        }
    }
}
