package exc.Board.repository;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberType;
import exc.Board.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    Member findMember;
    Member adminMember;

    @BeforeEach
    void 회원찾기(){
        //given
        Long adminId = 20230002L;
        Long findId = 20230006l;
        //when
        adminMember = memberRepository.find(adminId);
        findMember = memberRepository.find(findId);
/*        //then
        System.out.println("findMember = " + findMember);*/
    }

    @Test
    @Transactional
    @Commit
    void 회원가입(){
        //given
        Member member = new Member();
        member.setUserName("건주");
        member.setEmail("keon37@innotree.com");
        member.setMemberType(MemberType.USER);

        //DateEntity dataEntity = new DateEntity(adminMember.getId().toString(),LocalDateTime.now(), adminMember.getId().toString(),LocalDateTime.now() );
//        member.setDateEntity(dataEntity);

        //when (db 저장이 잘되었는지 -> save 값 이랑 find 값이랑 비교)
        Long saveId= memberRepository.save(member);
        Long findId = memberRepository.find(saveId).getId();

        //then
        Assertions.assertThat(findId).isEqualTo(member.getId());
    }

    @Test
    @Transactional
//    @Commit
    void 회원삭제(){
        memberRepository.delete(findMember.getId());
    }

    @Test
    @Transactional
    void 전체조회(){
        List<Member> memberList = memberRepository.findAll();
        for (Member member : memberList) {
            System.out.println("userId= " + member.getId() + ", userName = " + member.getUserName() + ", group = " + member.getMemberType() );
        }
    }
    @Test
    @Transactional
    void 이메일_중복체크(){
        //given
        //when
        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->memberService.isDuplicatedEmail(findMember));
//        System.out.println("e = " + e);
        assertThat(e.getMessage()).isEqualTo("이미 사용중인 이메일입니다.");
    }


    @Test
    @Transactional
    void 사용자명조회(){
        //given
        String userName = findMember.getUserName();
        //when
        Optional<Member> findNames = memberRepository.findByName(userName);
        System.out.println("findNames = " + findNames);
        // then
//        org.junit.jupiter.api.Assertions.assertEquals(userName, findNames);
    }
    @Test
    @Transactional
    void 사용자명전체조회(){
        //given
        String userName = findMember.getUserName();
        // when
        List<Member> findNames = memberRepository.findAllByName(userName);
        //then
        System.out.println("findNames = " + findNames);
    }
}