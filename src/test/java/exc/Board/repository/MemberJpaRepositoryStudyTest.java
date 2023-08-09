package exc.Board.repository;

import exc.Board.domain.member.Member;
import exc.Board.repository.bak.MemberJpaRepository_study;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class MemberJpaRepositoryStudyTest {

    @Autowired
    MemberJpaRepository_study memberJpaRepositoryStudy;

    @Test
    public void findByUserName(){
        Member m1 = new Member("AAA");
        Member m2 = new Member("BBB");
        memberJpaRepositoryStudy.save(m1);
        memberJpaRepositoryStudy.save(m2);

        List<Member> byUserName = memberJpaRepositoryStudy.findUser("AAA");
    }


/*    @Test
    public void findMemberForm(){
        Member m1 = new Member("AAA");
        memberJpaRepository.save(m1);
        List<MemberForm> memberForms = memberJpaRepository.findMemberForm();
        for (MemberForm dto : memberForms) {
            System.out.println("dto = " + dto);
        }
    }*/

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA");
        memberJpaRepositoryStudy.save(m1);
        List<Member> result = memberJpaRepositoryStudy.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = "+ member);
        }
    }

    @Test
    public void paging(){
        memberJpaRepositoryStudy.save(new Member("member1"));
        memberJpaRepositoryStudy.save(new Member("member2"));
        memberJpaRepositoryStudy.save(new Member("member3"));
        memberJpaRepositoryStudy.save(new Member("member4"));
        memberJpaRepositoryStudy.save(new Member("member5"));
        memberJpaRepositoryStudy.save(new Member("member6"));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));

        String userName = "AAA";
        // when
//        Page<Member> page = memberJpaRepository.findByUserName(userName, pageRequest);
        Slice<Member> page = memberJpaRepositoryStudy.findByUserName(userName, pageRequest); // limit + 1

        // Page<MemberDto> dtoPage =  page.map(m -> m.new MemberDto(m.getUSerName(), m.getId()))
//        long totalCount = memberJpaRepository.totalCount(userName);

        // then
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
//        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }
}


