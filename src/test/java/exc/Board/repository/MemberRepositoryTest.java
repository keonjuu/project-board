package exc.Board.repository;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.Role;
import exc.Board.service.MemberService;
import org.apache.juli.logging.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
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
        Long adminId = 1L;
        Long findId = 1001L;
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
        member.setRole(Role.USER);

        //DateEntity dataEntity = new DateEntity(adminMember.getId().toString(),LocalDateTime.now(), adminMember.getId().toString(),LocalDateTime.now() );
//        member.setDateEntity(dataEntity);

        //when (db 저장이 잘되었는지 -> save 값 이랑 find 값이랑 비교)
//        Long saveId= memberRepository.save(member);
//        Long findId = memberRepository.find(saveId).getId();

        //then
//        Assertions.assertThat(findId).isEqualTo(member.getId());
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
            System.out.println("userId= " + member.getId() + ", userName = " + member.getUserName() + ", group = " + member.getRole() );
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


    @Test
    @Transactional
    public void 페이징() throws Exception {
        //given

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<Member> page = memberRepository.findAll(pageRequest);
//        Slice<Member> slice = memberRepository.findAll(pageRequest);

        //then
        List<Member> memberList = page.getContent();
        System.out.println("memberList size = " + memberList.size());
        for (Member member : memberList) {
            System.out.println("member = " + member);
        }
    }
}