package project.toyproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.repository.ProductRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager em;

    @Test
    void 상품_등록() {

    }
}