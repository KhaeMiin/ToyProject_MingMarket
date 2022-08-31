package project.toyproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.repository.ProductRepository;
import project.toyproject.repository.SearchRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SearchServiceTest {

    @Autowired
    SearchService service;
    @Autowired
    SearchRepository searchRepository;
    @Autowired
    ProductService productService;
    @Autowired MemberService memberService;



}