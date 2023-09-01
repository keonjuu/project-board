package com.Board.Board.repository;

import com.Board.Board.dto.BoardForm;
import com.Board.Board.dto.CommentForm;
import com.Board.Board.dto.CommentRegisterForm;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.Comment;
import com.Board.Member.MemberRepository;
import com.Board.Member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
@Slf4j
class CommentRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;

    Board board;
    CommentForm commentForm;
    CommentRegisterForm childForm;

    Member member;
    @BeforeEach
    @DisplayName("게시글 초기화")
    void init(){
        commentForm = CommentForm.builder()
                .content("첫번째댓글")
                .isDeleted("N")
                .level(0)
                .boardNo(1137L)
                .build();
        board = boardRepository.findByBoardNo(commentForm.getBoardNo());
        member = memberRepository.find(Long.valueOf(20231039));


        childForm = CommentRegisterForm.builder()
                .content("1-3-1")
                .boardNo(1137L)
                .parentNo(1222L)
                .isDeleted("N")
                .regId(member.getEmail())
                .build();


    }

    public void printChildComments (Comment comment,int depth){
        List<Comment> childs = comment.getChilds();

        for (int i = 0; i < childs.size(); i++) {
            Comment child = childs.get(i);
            log.info("{}child{} = {}", getIndentation(depth), i, child.getId());
            printChildComments(child, depth + 1);
        }
    }

    public String getIndentation ( int depth){
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indentation.append("\t");
        }
        return indentation.toString();
    }
    @Test
    @DisplayName("계층확인")
    void findlevel() {

        Comment findComment = commentRepository.findById(1226L).orElse(null);
        log.info("findComment.getParent = {}", findComment.getParent());

        Comment findComment2 = commentRepository.findById(1211L).orElse(null);
        List<Comment> childs = findComment2.getChilds();

        Comment rootComment = findComment2; // 최상위 댓글 객체를 설정해야 함
        printChildComments(rootComment, 0); // 0은 초기 들여쓰기 레벨
    }


    @Test
    @DisplayName("계층확인")
    void findlevel2() {

        Comment findComment = commentRepository.findById(1211L).orElse(null);
        CommentForm form = CommentForm.toDTO(findComment);
        log.error("form = {}" , form);
    }

    @Test
    @DisplayName("마지막 계층 확인")
    void findlevel3() {

        Comment findComment = commentRepository.findById(1227L).orElse(null);
        CommentForm form = CommentForm.toDTO(findComment);

        log.error("마지막 댓글 form = {}" , form);

    }

    @Test
    @DisplayName("해당 게시글 내 댓글 목록 확인")
    void findByBoard_Comments(){
        Board board = boardRepository.findByBoardNo(1080L); // 댓글 0 건
//        Board board = boardRepository.findByBoardNo(1168L);
//        Board board = boardRepository.findByBoardNo(1137L);
        log.info("board 내의 댓글 = {}", board.getComments());

        // boardForm 으로 변경
        BoardForm boardForm = BoardForm.toDTO(board);
        log.error("boardForm = {}" , boardForm.getComments());

    }


    @Test
    @DisplayName("select 댓글")
    void findById(Long parentNo) {

        //given
        Comment comment = Comment.builder()
                .content(commentForm.getContent())
                .member(member)
                .board(board)
                .build();
//        log.info("comment.builder ={}" , comment);
        //when
        commentRepository.save(comment);

        // then
        Optional<Comment> findComment = commentRepository.findById(comment.getId());
        log.info("findComment = {}", findComment);

        Assertions.assertThat(findComment.get().getContent()).isEqualTo(comment.getContent());
    }

    @Test
    @DisplayName("삭제 업데이트")
    @Modifying
    @Transactional
    @Commit
    void delete(){
        commentRepository.updatedeleteContentById(1211L);
    }


    @Test
    @DisplayName("대댓글 추가")
    @Transactional
/*    @Commit*/
    void replySave(){
        log.info("childForm = {}", childForm);

        Comment child = Comment.builder()
                .content(childForm.getContent())
                .isDeleted("N")
//                .parent(commentRepository.findById(form.getParentNo()).orElse(null)) // 여기서 자동으로 댓글/대댓글 나뉠꺼 같은데...
                .build();

        // 부모와 자식 댓글 연관관계 설정
        Comment parent = commentRepository.findById(childForm.getParentNo()).orElse(null);

        child.setParent(parent);

        log.info("replaySave child.getParent = {}", child.getParent());
        log.info("parent.getChilds = {}", parent.getChilds());

  /*      // Board와 연관관계 설정
        Board board = boardRepository.findByBoardNo(childForm.getBoardNo());
        child.setBoard(board);

        //Member 연관관계 설정
        Member member = memberRepository.findByEmail(childForm.getRegId()).orElse(null);
        child.setMember(member);

*/
        log.info("계층구조 = {}", child);

        commentRepository.save(child);
    }

    @Test
    @DisplayName("댓글 계층구조로 정렬")
    void sortComments(){
        //given  (게시판의 댓글리스트를 dto 로 반환)
        List<CommentForm> flatComments = boardRepository.findByBoardNo(1137L)
                .getComments()
                .stream()
                .map(comment -> CommentForm.toDTO(comment))
                .sorted(Comparator.comparing(CommentForm::getLevel)
                        .thenComparing(CommentForm::getModTime))
                .collect(Collectors.toList());

        // map<id, dto>
        Map<Long, CommentForm> formMap = new HashMap<>();
        for (CommentForm form : flatComments) {
            formMap.put(form.getId(), form);
        }

        // when (result 계층구조로 만들기)
        List<CommentForm> result  = new ArrayList<>();
        for (CommentForm commentForm : flatComments) {

            CommentForm parent = formMap.get(commentForm.getParentNo());

            if (parent !=null){ // 자식이면 부모에 추가
                formMap.get(commentForm.getParentNo()).getChilds().add(commentForm);
            }else{ // 부모면 추가
                result.add(commentForm);
            }
        }

/* (case1)
        Map<Long, CommentForm> commentFormMap = new HashMap<>();
        List<CommentForm> result  = new ArrayList<>();

        boardRepository.findByBoardNo(1137L)
                .getComments()
                .stream()
                .forEach(comment -> {
                    CommentForm form = CommentForm.toDTO(comment);
                    commentFormMap.put(form.getId(), form);
                    if(comment.getParent() != null){
                        commentFormMap.get(comment.getParent().getId()).getChilds().add(form);
                    }else{
                        result.add(form);
                    }
                });*/

        //then
        log.info("### 계층구조  = {} ", result.stream().map(f-> f.getId()).collect(Collectors.toList()) );

    }










    @Test
    @DisplayName("대댓글 삭제")
    @Transactional
    @Commit
    void replyDelete(){
        List<Comment> childAll = commentRepository.findAllCommentByParentNo(1224L);
        log.info("childAll = {}" , childAll);
        // 자식이 존재하면 isDeleted = true
        if(childAll.size()!=0){
            commentRepository.updatedeleteContentById(1224L); // 나만 isDeleted = 'Y'
        }else {
            // 자식이 존재하지 않으면 자신을 삭제
            commentRepository.deleteById(1224L); // 나만 삭제
            // 부모와 다른 자식들이 없는지 확인하고 삭제가능한 조상 찾기

        }
//        commentRepository.findById(commentForm.getParentNo()).ifPresent(parentComment -> commentRepository.findById(parentComment.getpa()));

    }

}
