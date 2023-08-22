package exc.Board.repository;

import exc.Board.controller.Member.MemberForm;
import exc.Board.domain.board.Board;
import exc.Board.domain.member.Member;
import exc.Board.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
public class MemberRepositoryTest {
    private static Logger logger = LoggerFactory.getLogger(MemberRepositoryTest.class);

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    Member findMember;
    Member adminMember;

    @BeforeEach
    void 회원찾기(){
        //given
        Long adminId = 20230005L;
        Long findId = 20231010L;
        //when
        adminMember = memberRepository.find(adminId);
        findMember = memberRepository.find(findId);
/*        //then
        System.out.println("findMember = " + findMember);*/
    }

    @Test
    void toEntity(){
        MemberForm form = MemberForm.builder()
                .name("pkj")
                .email("keonjuu")
                .pwd("kkk")
                .build();
        System.out.println("MemberForm = " + form);
        Member entity= form.toEntity(form);
        /*Member entity = Member.toEntity(form);*/
        logger.info("entity = {}" , entity);
    }

    @Test
    @Transactional
    @Commit
    void 회원가입(){
        //given
/*        Member member = new Member();
        member.setUserName("건주");
        member.setEmail("keon37@innotree.com");
        member.setPassword("kkk");
        member.setRole(Role.USER);*/
        MemberForm form = MemberForm.builder()
                .name("dtoMember")
                .email("dtoMember@inno")
                .pwd("dtoMember")
                .build();
        Member entity = form.toEntity(form);
        logger.info("form = {}", form);

        // when
        memberRepository.save(entity);
        Member member = memberRepository.find(entity.getId());

        // then
        Assertions.assertThat(member.getEmail()).isEqualTo(form.getEmail());
    }

    @Test
//    @Commit
    void 회원삭제(){
        memberRepository.delete(findMember.getId());
    }

    @Test
    void 전체조회(){
        List<Member> memberList = memberRepository.findAll();
        for (Member member : memberList) {
            System.out.println("userId= " + member.getId() + ", userName = " + member.getUserName() + ", group = " + member.getRole() );
        }
    }
    @Test
    void 이메일_중복체크(){
        //given
        //when
        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->memberService.isDuplicatedEmail(findMember));
//        System.out.println("e = " + e);
        assertThat(e.getMessage()).isEqualTo("이미 사용중인 이메일입니다.");
    }


    @Test
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
    void 사용자명전체조회(){
        //given
        String userName = findMember.getUserName();
        // when
        List<Member> findNames = memberRepository.findAllByName(userName);
        //then
        System.out.println("findNames = " + findNames);
    }


    @Test
    public void 페이징() throws Exception {
        //given

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<MemberForm> page = memberRepository.findAll(pageRequest)
                .map(member -> MemberForm.builder()
                            .email(member.getEmail())
                            .name(member.getUserName())
                            .pwd(member.getPassword())
                                .build()
/*              {      MemberForm form = new MemberForm();
                    form.setEmail(member.getEmail());
                    form.setName(member.getUserName());
                    form.setPwd(member.getPassword());
                    return form;
                }*/
                );
//        Slice<Member> slice = memberRepository.findAll(pageRequest);

        //then
        List<MemberForm> memberList = page.getContent();
        System.out.println("memberList size = " + memberList.size());
        for (MemberForm member : memberList) {
            System.out.println("member = " + member);
        }
    }


    @Test
    public void 사용자글목록조회(){
        // when
        List<Board> boards = findMember.getBoards();
        for (Board board : boards) {
            System.out.println(board);
        }

        Page<Board> memberBoards = (Page<Board>)findMember.getBoards();
        boards.forEach(m -> m.getMember().getUserName());

        System.out.println( "boards= "+ boards);

        /*for (Board board : pageList) {
            board.getMember().getUserName();
        }*/

        // then
//        Assertions.assertThat(boards.size()).isEqualTo(4);
    }


/*    @Test
    public void 페이징사용자글목록조회() {
        //when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Member> page = memberRepository.findALLWithBoardList(findMember.getId(), pageRequest);

    }*/


}