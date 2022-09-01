package project.toyproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.CategoryList;
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
import project.toyproject.dto.ProductDto;
import project.toyproject.repository.ProductJpaRepository;
import project.toyproject.repository.ProductRepository;
import project.toyproject.repository.SearchRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static project.toyproject.dto.ProductDto.*;

@SpringBootTest
@Transactional
class SearchServiceTest {

    @Autowired
    SearchService searchService;
    @Autowired
    SearchRepository searchRepository;
    @Autowired
    ProductJpaRepository productJpaRepository;
    @Autowired MemberService memberService;

    @DisplayName("상품 검색")
    @Test
    void searchPost() {
        //given
        createProducts();
        //when
        List<SelectProducts> search = searchService.searchPosts("te");
        System.out.println("search.size() = " + search.size());
        //then
        assertThat(search.size()).isEqualTo(3);
    }

    private void createProducts() {
        Member member = createMember();
        Product product1 = Product.createProduct("test1", "test1.jpg", "test1", 10000, member, CategoryList.FASHION);
        Product product2 = Product.createProduct("test2", "test2.jpg", "test2", 30000, member, CategoryList.FASHION);
        Product product3 = Product.createProduct("test3", "test3.jpg", "test3", 20000, member, CategoryList.FOOD);
        Product product4 = Product.createProduct("밍마켓베스트", "test3.jpg", "test3ㄹㄹㄹㄹ", 20000, member, CategoryList.FOOD);
        Product product5 = Product.createProduct("너무 좋은 베", "test3.jpg", "test3ㅈㅈㄷㅂㅁㅈㄷㅈㅇㅌㄴ", 20000, member, CategoryList.FOOD);
        productJpaRepository.save(product1);
        productJpaRepository.save(product2);
        productJpaRepository.save(product3);
        productJpaRepository.save(product4);
        productJpaRepository.save(product5);
    }


    private Member createMember() {
        MemberDto.CreateMemberForm member = new MemberDto.CreateMemberForm();
        member.createMethod("test1member", "min",
                "test12345+", "test12345+", "해민", 0100000000, "성수동", "밍마켓 123-4");
        return memberService.join(member);
    }

}