package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 다대다 관계가 아닌 다대일관계의 카테고리를 만들고싶다.
 * 어떻게 해야하는지 감이 안잡힘.
 * 일대일관계 (하나의 상품엔 하나의 카테고리가 포함된다.)
 * 카테고리 리스트는 Enum 클래스 사용
 * 상위 카테고리(private List<Category> parent; 사용 안함.
 */
@Entity
@Getter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId; //pk

    private String categoryName;

    private Long parentId; //NotNull (기본값 0이 들어가도록)


}
