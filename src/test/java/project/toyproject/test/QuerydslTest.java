package project.toyproject.test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.QQueryTest;
import project.toyproject.domain.QueryTest;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static project.toyproject.domain.QQueryTest.*;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        QueryTest test1 = new QueryTest("name1", 10);
        QueryTest test2 = new QueryTest("name2", 20);
        QueryTest test3 = new QueryTest("name3", 30);
        QueryTest test4 = new QueryTest("name4", 40);
        em.persist(test1);
        em.persist(test2);
        em.persist(test3);
        em.persist(test4);
    }

    @Test
    void select() {
        QueryTest findTest = queryFactory
                .selectFrom(queryTest)
                .where(queryTest.name.eq("name1"))
                .fetchOne();
        assertThat(findTest.getName()).isEqualTo("name1");
    }

    @Test
    void select2() {
        List<QueryTest> findTest = queryFactory
                .selectFrom(queryTest)
                .where(queryTest.name.ne("name1")) //name != name1
                .fetch();
        assertThat(findTest.size()).isEqualTo(3);
    }
}
