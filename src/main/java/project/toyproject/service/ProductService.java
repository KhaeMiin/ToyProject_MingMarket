package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.MemberRepository;
import project.toyproject.repository.ProductJpaRepository;
import project.toyproject.repository.ProductRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductJpaRepository productJpaRepository;
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public Long saveProduct(Long memberId, String title, String thumbnail, String intro, int price, CategoryList categoryList) {

        //엔티티 조회
//        Member member = memberRepository.findOneMember(memberId);
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> {
            throw new IllegalStateException("저장된 값이 없습니다.");
        });

        //상품 생성
        Product product = Product.createProduct(title, thumbnail, intro, price, member, categoryList);

        //상품 저장
//        productRepository.save(product); //순수 JPA
        productJpaRepository.save(product); //Spring DATA JPA

        return product.getId();
    }

    /**
     * 상품 수정
     * JPA 변경 감지를 활용하여 update.
     * 트렌젝션이 종료될 때 변경된 부분에 대한 update query를 날린다.
     */
    @Transactional
    public void updateProduct(Long productId, UpdateProductForm form, String thumbnail) {
//        Product findProduct = productRepository.findSingleProduct(productId); //순수 JPA
        Product findProduct = productJpaRepository.findById(productId).orElseThrow(() -> {  //Spring DATA JPA
            throw new IllegalStateException("저장된 값이 없습니다.");
        });
        findProduct.change(form.getTitle(), thumbnail, form.getIntro(), form.getPrice());
    }

    /**
     * 모든 상품 조회
     */
    public List<SelectProducts> findProducts() {
//        List<Product> products = productRepository.findAllProducts(); //순수 JPA
        List<Product> products = productJpaRepository.findAll(); //Spring DATA JPA
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
     * 모든 상품 조회 (페이징처리)
     */
    public Page<SelectProducts> findProductsPage(Pageable pageable) {
        Page<Product> products = productJpaRepository.findAll(pageable);//Spring DATA JPA

        //stream 사용
        return products.map(p -> new SelectProducts(p));

    }

    /**
     * 단일 상품 조회
     * TODO
     * Spirng Data JPA 경우 ProductDetailData에 값 넣을 때 product.getMember().getUserId();, getMember().getNickname();부분
     * 부분 n+1 다시 생각해보기
     * fetch Joing (@EntityGraph)을 사용하여 이 부분 해결(한방쿼리 사용_)
     */
    public ProductDetailData findSingleProduct(Long productId) {
//        Product findProduct = productRepository.findSingleProduct(productId); //순수 JPA
        Product findProduct = productJpaRepository.findById(productId).orElseThrow(() -> { //Spring DATA JPA
            throw new IllegalStateException("저장된 값이 없습니다.");
        });

        //게시글 작성 날짜 구하기
        String createDate = findProduct.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        ProductDetailData singleProduct = new ProductDetailData(findProduct, createDate); //이 부분에서 product.getMember().getUserId();때문에 n+1의 문제가 발생할 수 있다. fetch로 수정해야함

        return singleProduct;
    }

    /**
     * 삭제
     * @param productId
     */
    @Transactional
    public void removeProduct(Long productId) {
//        productRepository.removeProduct(productId); //순수 JPA
        productJpaRepository.deleteById(productId); //Spring DATA JPA
    }

    /**
     * 내가 올린 상품 리스트
     * TODO
     * SelectProducts: product.getMember().getId();부분 n+1 다시 생각해보기
     * getMember().nickName()등 다른 정보를 꺼낼 일이 있으면 fetch join
     * getId()외 다른 정보를 꺼낼 일이 없으면 fetch join을 굳이 안해도 될 것 같다. (개인적인 내 생각)
     */
    public List<SelectProducts> userProductsList(Long memberId) {
//        List<Product> products = productRepository.userProducts(memberId); //순수 JPA
        List<Product> products = productJpaRepository.findByMemberId(memberId);
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
