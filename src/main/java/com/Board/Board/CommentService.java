package com.Board.Board;

import com.Board.Board.dto.CommentRegisterForm;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.Comment;
import com.Board.Board.repository.BoardRepository;
import com.Board.Board.repository.CommentRepository;
import com.Board.Member.MemberRepository;
import com.Board.Member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    public final CommentRepository commentRepository;
    public final BoardRepository boardRepository;
    public final MemberRepository memberRepository;

    @Transactional
    public void save(CommentRegisterForm form){
        Comment comment = Comment.builder()
                .content(form.getContent())
                .level(form.getParentNo() == null? 1 :form.getLevel()+1)
                .build();

        // 부모와 자식 댓글 연관관계 설정
        // 첫댓글 아니면
        if(form.getParentNo() != null ) {
            Comment parent = findById(form.getParentNo());
            comment.setParent(parent);
        }
        // Board와 연관관계 설정
//        if(form.getBoardNo() == null) {
            Board board = boardRepository.findByBoardNo(form.getBoardNo());
            comment.setBoard(board);
//        }
//        if(form.getRegId() == null) {
            //Member 연관관계 설정
            Member member = memberRepository.findByEmail(form.getRegId()).orElse(null);
            comment.setMember(member);
//        }

        log.info("comment.getMember = {}", comment.getMember());

        commentRepository.save(comment);
    }

    public Comment findById(Long commentNo) {
        return commentRepository.findById(commentNo).orElse(null);
    }

    @Transactional
    public void deleteById(Long commnetNo){

        // 대댓글 존재하면 내용 삭제
        commentRepository.updateContentById(commnetNo);
        //
//        commentRepository.deleteById(commnetNo);
    }
}
