package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Product;
import project.toyproject.repository.ProductRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // 모든 상품 조회
    public List<Product> findProducts() {
        return productRepository.findAllProducts();
    }

    //상품 단일 조회
    public Product findOneProduct(Long productId) {
        return productRepository.findOneProduct(productId);
    }
}
