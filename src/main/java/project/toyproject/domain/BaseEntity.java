package project.toyproject.domain;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 상속관계 매핑 아님
 * 이 클래스를 상속받은 자식 클래스에 매핑 정보만 제공함.
 * 직접 생성해서 사용할 일이 없으니(사용하면 안됨) 추상클래스로 만듬
 * em.find(BaseEntity) 이런식으로 조회 불가능 (엔티티가 아님)
 */

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    private LocalDateTime createDate; //상품 생성 날짜나 회원가입된 날짜.

    public void createDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
