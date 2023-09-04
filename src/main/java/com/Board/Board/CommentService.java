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

import java.util.stream.Collectors;

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
                .isDeleted("N")
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
    public void deleteByComment(Long commnetNo){
//        Comment comment= commentRepository.findCommentByIdWithParent(commnetNo).orElse(null);
        //List<Comment> childAll = commentRepository.findAllCommentByParentNo(commnetNo);

        Comment comment = findById(commnetNo);
        // 부모가 자식 없으면 바로 삭제(자식이 존재하면 isDeleted = "Y")
//        if(childAll.size()!=0){
        if(comment.getChildren().size()!=0){
            commentRepository.updatedeleteContentById(commnetNo); // 나만 isDeleted = 'Y'
        }else {
            // 부모와 다른 자식들이 없는지 확인하고 삭제가능한 부모 찾기
            commentRepository.delete(searchDeletableComment(comment));
        }
        log.info("===== comment.getId ====>  {}" , comment.getId());
//        log.info("comment.getChildren() = {}" , comment.getChildren().stream().map(c-> c.getId()).collect(Collectors.toList()));

    }

    public Comment searchDeletableComment(Comment comment){

        // 나말고 다른자식 없는 부모가 삭제되어 있으면 삭제 대상
        Comment parent = comment.getParent(); //--> 프록시 객체
//        Comment parent = commentRepository.findById(comment.getParent().getId()).orElse(null);
//        log.info("탐색 start ==> comment {}" , comment.getId());

        if(parent !=null && parent.getChildren().size() == 1 && parent.getIsDeleted().equals("Y")){
            log.info("자식이 나밖에 없네~ 삭제!!! ");

            // 부모 더 확인
            return searchDeletableComment(parent);
        }
        else if (parent !=null ){
            log.info("Before :: comment.getParent().getChildren() id = {}" , comment.getParent().getChildren().stream().map(c-> c.getId()).collect(Collectors.toList()));
            comment.getParent().getChildren().remove(comment);//부모와 연관관계 삭제
            log.info("After :: comment.getParent().getChildren() id = {}" , comment.getParent().getChildren().stream().map(c-> c.getId()).collect(Collectors.toList()));
        }

        return comment;
    }

}


