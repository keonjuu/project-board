package com.Board.repository;

import com.Board.Board.entity.Board;
import com.Board.Member.MemberRepository;
import com.Board.Member.MemberService;
import com.Board.Member.dto.MemberForm;
import com.Board.Member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
@Transactional(readOnly = true)
class MemberRepositoryTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    Member findMember;
    Member adminMember;

    @BeforeEach
    @DisplayName("회원찾기")
    void findMember(){
        //given
        Long adminId = 20230005L;
        Long findId = 20231010L;
        //when
        adminMember = memberRepository.find(adminId);
        findMember = memberRepository.find(findId);
/*        //then
        log.info("findMember = {}", findMember);*/
    }

    @Test
    void toEntity(){
        MemberForm form = MemberForm.builder()
                .name("pkj")
                .email("keonjuu")
                .pwd("kkk")
                .build();
        log.info("MemberForm = {}" , form);
        Member entity= form.toEntity(form);
        /*Member entity = Member.toEntity(form);*/
        log.info("entity = {}" , entity);
    }

    @Test
    @Transactional
    @DisplayName("회원가입")
    void join(){
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
        log.info("form = {}", form);

        // when
        memberRepository.save(entity);
        Member member = memberRepository.find(entity.getId());

        // then
        Assertions.assertThat(member.getEmail()).isEqualTo(form.getEmail());
    }

    @Test
    @DisplayName("회원삭제")
//    @Commit
    void delete(){
        memberRepository.delete(findMember.getId());
    }

    @Test
    @DisplayName("전체조회")
    void findAll(){
        List<Member> memberList = memberRepository.findAll();
        for (Member member : memberList) {
            log.info("userId= {}, userName = {}, role = {}", member.getId(), member.getUserName(), member.getRole());
        }
    }
    @Test
    @DisplayName("이메일_중복체크")
    void isDuplicatedEmail(){
        //given
        //when
        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->memberService.isDuplicatedEmail(findMember));
//        log.info("e = {}" + e);
        assertThat(e.getMessage()).isEqualTo("이미 사용중인 이메일입니다.");
    }


    @Test
    @DisplayName("사용자명조회")
    void findByName(){
        //given
        String userName = findMember.getUserName();
        //when
        Optional<Member> findNames = memberRepository.findByName(userName);
        log.info("findNames = {}", findNames);
        // then
//        org.junit.jupiter.api.Assertions.assertEquals(userName, findNames);
    }
    @Test
    @DisplayName("사용자명전체조회")
    void findAllByName(){
        //given
        String userName = findMember.getUserName();
        // when
        List<Member> findNames = memberRepository.findAllByName(userName);
        //then
        log.info("findNames = {}", findNames);
    }


    @Test
    @DisplayName("페이징")
    void paging(){
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
        log.info("memberList size = {}" , memberList.size());
        for (MemberForm member : memberList) {
            log.info("member = {}" , member);
        }
    }


    @Test
    @DisplayName("사용자글목록조회")
    void findMember_boards(){
        // when
        List<Board> boards = findMember.getBoards();
        for (Board board : boards) {
            log.info("board={}",board);
        }

        boards.forEach(m -> m.getMember().getUserName());

        log.info("boards={}", boards);

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