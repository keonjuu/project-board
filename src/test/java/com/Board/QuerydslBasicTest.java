package com.Board;

import com.Board.Member.dto.MemberForm;
import com.Board.Member.entity.Member;
import com.Board.Member.entity.MemberStatus;
import com.Board.Member.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BoardApplication.class)
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    private QMember member = QMember.member;

    @Test
    public void sort() {

        List<Member> results = queryFactory
                .selectFrom(member)
                .where(member.status.eq(MemberStatus.PENDING))
                .orderBy(member.id.desc(), member.lastDatetime.asc().nullsLast())
                .fetch();

        List<String> strings = results
                .stream()
                .map(member1 -> {
                    String id = String.valueOf(member1.getId());
                    String userName = member1.getUserName();
                    return "id =" + id + ", userName = " + userName;
                })
                .collect(Collectors.toList());

        for (String str : strings) {
            log.info("str = {}", str);
        }
    }


    @Test
    public void tupleProjection() {
        List<Tuple> result = queryFactory
                .select(member.id, member.userName)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            Long id = tuple.get(member.id);
            String email = tuple.get(member.email);
            log.info("id = {}", id);
            log.info("email = {}", email);
        }
    }


    // 순수 jpa 에서 dto 조회
    // new 명령어 사용
    // package 이름을 다 적어줘야 함
    // 생성자 방식만 지원함
    @Test
    public void findDtoByJPQL() {
        List<MemberForm> result =
                em.createQuery("select new com.Board.Member.dto.MemberForm(m.userName, m.email) " +
                                "from Member m", MemberForm.class)
                        .getResultList();
    }


    // setter 를 활용해서 querydsl 작성
    // select(Projections.bean(MemberForm.class, ___, ___)
    @Test
    public void findDtoBySetter() {
        List<MemberForm> memberForms = queryFactory
                .select(Projections.bean(MemberForm.class,
                        member.userName,
                        member.email))
                .from(member)
                .fetch();

        for (MemberForm memberForm : memberForms) {
            log.info("memberFrom = {}", memberForm);
        }
    }
    // filed 를 활용해서 querydsl 작성
    // select(Projections.fields(MemberForm.class, ___, ___)
    @Test
    public void findDtoByField() {
        List<MemberForm> memberForms = queryFactory
                .select(Projections.fields(MemberForm.class,
                        member.userName,
                        member.email))
                .from(member)
                .fetch();

        for (MemberForm memberForm : memberForms) {
            log.info("memberFrom = {}", memberForm);
        }
    }


    @Test
    public void findDtoByConstructor() {
        List<MemberForm> memberForms = queryFactory
                .select(Projections.constructor(MemberForm.class,
                        member.userName,
                        member.email))
                .from(member)
                .fetch();

        for (MemberForm memberForm : memberForms) {
            log.info("memberFrom = {}", memberForm);
        }
    }

}