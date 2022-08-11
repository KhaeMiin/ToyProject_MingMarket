package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.domain.WishItem;
import project.toyproject.dto.ProductDto;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.ProductRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;

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
    public Long saveProduct(Long memberId, String title, String thumbnail, String intro, int price, CategoryList categoryList) {

        //엔티티 조회
        Member member = memberRepository.findOneMember(memberId);

        //상품 생성
        Product product = Product.createProduct(title, thumbnail, intro, price, member, categoryList);

        //상품 저장
        productRepository.save(product);

        return product.getId();
    }

    /**
     * 상품 수정
     * JPA 변경 감지를 활용하여 update.
     * 트렌젝션이 종료될 때 변경된 부분에 대한 update query를 날린다.
     */
    @Transactional
    public void updateProduct(Long productId, UpdateProductForm form, String thumbnail) {
        Product findProduct = productRepository.findSingleProduct(productId);
        findProduct.change(form.getTitle(), thumbnail, findProduct.getIntro(), form.getPrice());

    }

    /**
     * 모든 상품 조회
     */
    public List<SelectProducts> findProducts() {
        List<Product> products = productRepository.findAllProducts();
/*        List<ProductDto.SelectProducts> productList = new ArrayList<>();
        for (Product product : products) {
            SelectProducts selectProductData = new SelectProducts(
                    product.getId(), product.getTitle(), product.getThumbnail(),
                    product.getIntro(), product.getPrice(), product.getMember(),
                    product.getProductStatus());
            productList.add(selectProductData);
        }*/
        //stream 사용
        List<SelectProducts> productList = products.stream()
                .map(p -> new SelectProducts(p)).collect(Collectors.toList());
        return productList;
    }

    /**
     * 단일 상품 조회
     */
    public ProductDetailData findSingleProduct(Long productId) {
        Product findProduct = productRepository.findSingleProduct(productId);

        //게시글 작성 날짜 구하기
        String createDate = findProduct.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        ProductDetailData singleProduct = new ProductDetailData(productId, findProduct.getMember().getNickname(), findProduct.getMember().getUserId(),
                findProduct.getTitle(), findProduct.getThumbnail(), findProduct.getIntro(), findProduct.getPrice(),
                createDate);

        return singleProduct;
    }

    @Transactional
    public void removeProduct(Long productId) {
        productRepository.removeProduct(productId);
    }

    /**
     * 내가 올린 상품 리스트
     */
    public List<SelectProducts> userProductsList(Long memberId) {
        List<Product> products = productRepository.userProducts(memberId);
/*        List<SelectProducts> userProductList = new ArrayList<>();
        for (Product product : products) {
            SelectProducts selectProductData = new SelectProducts(
                    product.getId(), product.getTitle(), product.getThumbnail(),
                    product.getIntro(), product.getPrice(), product.getMember(),
                    product.getProductStatus());
            userProductList.add(selectProductData);
        }*/
        //stream 사용
        List<SelectProducts> userProductList = products.stream()
                .map(p -> new SelectProducts(p)).collect(Collectors.toList());
        return userProductList;
    }

}
