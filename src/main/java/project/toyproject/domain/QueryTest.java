package project.toyproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class QueryTest {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int age;

    public QueryTest(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
