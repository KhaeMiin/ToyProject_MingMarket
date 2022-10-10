package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Product;
import project.toyproject.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    @Transactional
    public List<SelectProducts> searchPosts(String keyword) {
        List<Product> productEntities = searchRepository.findByTitleContaining(keyword);
        List<SelectProducts> productDtoList = new ArrayList<>();


        if (productEntities.isEmpty()) {
            return productDtoList;
        }
        /*for (Product productEntity : productEntities) {
            SelectProducts selectProducts = new SelectProducts(productEntity);
            productDtoList.add(selectProducts);
        }
        */
        return productEntities.stream()
                .map(p -> new SelectProducts(p))
                .collect(Collectors.toList());

//        return productDtoList;
    }

    //페이징 처리
    @Transactional
    public Page<SelectProducts> searchPostsPage(String keyword, Pageable pageable) {
        Page<Product> products = searchRepository.findByTitleContaining(keyword, pageable);

        if (products.isEmpty()) {
            return products.map(p -> new SelectProducts(p));
        }

        return products
                .map(p -> new SelectProducts(p));

    }
}
