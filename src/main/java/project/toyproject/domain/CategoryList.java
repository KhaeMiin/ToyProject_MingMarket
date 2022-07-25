package project.toyproject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 무한 카테고리 구현이 아닌 단순한 카테고리를 구현할 경우
 * 카테고리 테이블을 따로 만들지 않고 ENUM으로 Product 엔티티에 필드선언하기. (컬럼이 category로 추가되는 것 뿐임)
 */
@Getter
@RequiredArgsConstructor //value값 String으로 넣을 수 있도록 자동 주입
public enum CategoryList {

    FASHION("패션"), FOOD("음식"), DIGITAL_APPLIANCES("디지털/가전"),
    BOOKS("도서");

    private final  String categoryName;

}
