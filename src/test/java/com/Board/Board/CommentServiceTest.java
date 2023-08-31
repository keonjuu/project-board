package com.Board.Board;

import com.Board.Board.dto.CommentRegisterForm;
import com.Board.Board.entity.Comment;
import com.Board.Member.MemberService;
import com.Board.Member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
@Slf4j
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    MemberService memberService;
    CommentRegisterForm form;
    @BeforeEach
    @DisplayName("댓글 초기화 - CommentForm")
    void init(){
        Member member = memberService.findById(20231024L);
        form = CommentRegisterForm.builder()
                .content("1-2-1-3")
                .boardNo(1137L)
                .parentNo(1225L)
                .regId(member.getEmail())
                .build();

//        board = boardRepository.findByBoardNo(commentForm.getBoardNo());
    }

    @Test
    @Transactional
    @Commit
    @DisplayName("댓글저장")
    void save(){
        commentService.save(form);
    }

    @Test
    @Transactional
    @DisplayName("대댓글 저장")
    @Commit
    void replaySave(){

        log.info("form = {}", form);
        commentService.save(form);
    }

    @Test
    @Transactional
    @DisplayName("대댓글 삭제")
    void replayDelete(){

        log.info("form = {}", form);
//        commentService.replaySave(form);
    }

    @Test
    @DisplayName("댓글번호 찾기")
    void findId(){
        Comment comment = commentService.findById(1211L);
        log.info("comment = {}" , comment);
    }

    /*
    @Test
    @Transactional
    void delete(){
        commentRepository.updateContentById(commnetNo);
    }
*/

}