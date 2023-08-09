package exc.Board.repository;

import exc.Board.domain.Board;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
public class MemberRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    Member findMember;
    Member adminMember;

    @BeforeEach
    void 회원찾기(){
        //given
        Long adminId = 20230005L;
        Long findId = 20230006L;
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
        member.setPassword("kkk");
        member.setRole(Role.USER);

        memberRepository.save(member);

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


    @Test
    public void 사용자글목록조회(){
        // when
        List<Board> boardList = findMember.getBoardList();
        for (Board board : boardList) {
            System.out.println(board);
        }
        // then
        Assertions.assertThat(boardList.size()).isEqualTo(4);
    }


/*    @Test
    public void 페이징사용자글목록조회() {
        //when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Member> page = memberRepository.findALLWithBoardList(findMember.getId(), pageRequest);

    }*/


}