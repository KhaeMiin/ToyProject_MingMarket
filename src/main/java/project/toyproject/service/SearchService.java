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
import java.util.stream.Collectors;

import static project.toyproject.dto.ProductDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    /**
     * product에 대한 select Query 한 번만 나감
     */
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
                .map(p -> new SelectProducts(p.getId(), p.getTitle(), p.getThumbnail(),
                        p.getIntro(), p.getPrice(), p.getMember().getId(), p.getProductStatus()))
                .collect(Collectors.toList());

//        return productDtoList;
    }
}
