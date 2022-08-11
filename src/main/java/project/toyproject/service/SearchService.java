package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Product;
import project.toyproject.dto.ProductDto;
import project.toyproject.repository.ProductRepository;
import project.toyproject.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;

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
        for (Product productEntity : productEntities) {
            SelectProducts selectProducts = new SelectProducts(productEntity);
            productDtoList.add(selectProducts);
        }

        return productDtoList;
    }
}
