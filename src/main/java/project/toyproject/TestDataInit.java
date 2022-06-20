package project.toyproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.service.MemberService;
import project.toyproject.service.ProductService;

import javax.annotation.PostConstruct;

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
        Address address = new Address("자바시 JPA구", "스프링");
        Member member = new Member("test", "테스트", "test", "김테스트", 01022223333, address);
        memberService.join(member);

//        Product product = new Product("test", "");
        for (int i = 1; i < 17; i++) {
            productService.saveProduct(member.getId(), "test" + i, i + ".jpg", "test" + i, 10000);
        }
    }
}
