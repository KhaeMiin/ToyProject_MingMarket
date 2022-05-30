package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.ProductRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public Long saveProduct(Long memberId, String title, String thumbnail, String intro, int price) {

        //엔티티 조회
        Member member = memberRepository.findOneMember(memberId);

        //상품 생성
        Product product = Product.createProduct(title, thumbnail, intro, price, member);

        //상품 저장
        productRepository.save(product);

        return product.getId();
    }

    // 모든 상품 조회
    public List<Product> findProducts() {
        return productRepository.findAllProducts();
    }

    //상품 단일 조회
    public Product findSingleProduct(Long productId) {
        return productRepository.findSingleProduct(productId);
    }
}
