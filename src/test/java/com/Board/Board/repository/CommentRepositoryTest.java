package com.Board.Board.repository;

import com.Board.Board.dto.BoardForm;
import com.Board.Board.dto.CommentForm;
import com.Board.Board.dto.CommentRegisterForm;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.Comment;
import com.Board.Board.entity.QComment;
import com.Board.Member.MemberRepository;
import com.Board.Member.entity.Member;
import com.Board.Member.entity.QMember;
import com.Board.Member.entity.Role;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
@Slf4j
class CommentRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Board board;
    private CommentForm commentForm;
    private CommentRegisterForm childForm;

    private Member member;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager em;
    private QComment comment = QComment.comment;

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
        log.info("parent.getChildren = {}", parent.getChildren());

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
                formMap.get(commentForm.getParentNo()).getChildren().add(commentForm);
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
                        commentFormMap.get(comment.getParent().getId()).getChildren().add(form);
                    }else{
                        result.add(form);
                    }
                });*/

        //then
        log.info("### result = {}", result);
        log.info("### 계층구조  = {} ", result.stream().map(f-> f.getId()).collect(Collectors.toList()) );
    }


    @Test
    @DisplayName("대댓글 삭제")
    @Transactional
    @Commit
    void replyDelete(){
//        List<Comment> childAll = commentRepository.findAllCommentByParentNo(1224L);
        Comment comment = commentRepository.findCommentByIdWithParent(1224L).orElse(null);
        List<Comment> childAll = comment.getChildren();
        log.info("childAll = {}" , childAll);
        // 자식이 존재하면 isDeleted = true
        if(childAll.size()!=0){
            commentRepository.updatedeleteContentById(1224L); // 나만 isDeleted = 'Y'
        }else {
            // 자식이 존재하지 않으면 자신을 삭제
            commentRepository.deleteById(1224L); // 나만 삭제
            // 부모와 다른 자식들이 없는지 확인하고 삭제가능한 조상 찾기
            // 구현
        }
    }


    @Test
    @DisplayName("게시글 해당 댓글 조회 ")
    void hierarchy(){
        Long boardNo = 1137L;
        List<Comment> allByBoardNo = commentRepository.findAllByBoardNo(boardNo);
        final List<Long> commentIDs = allByBoardNo.stream().map(comment -> comment.getId()).collect(Collectors.toList());

        log.info("allByBoardNo.size = {}" , allByBoardNo.size());
        log.info("{}", commentIDs);
    }



    @Test
    public void searchId(){
        Comment comment = queryFactory
                .selectFrom(this.comment)
                .where(this.comment.id.eq(1304L))
                .fetchOne();

        log.info("comment = {}" , comment);
    }

    @Test
    @DisplayName("fetchJoin 미사용")
    public void findAllByBoardNoFetchNoUse() {

        Long boardNo = 1137L;
        List<Comment> result =
                queryFactory.selectFrom(comment)
                        .leftJoin(comment.parent)
                        .where(comment.board.boardNo.eq(boardNo))
                        .orderBy(comment.parent.id.asc().nullsFirst(),
                                comment.regTime.asc()
                        ).fetch();
        log.info("result = {}" , result);

    }

    @Test
    @DisplayName("fetchJoin 사용")
    public void findAllByBoardNoFetchUse(){

        Long boardNo = 1137L;
        // Projections.fields(Comment.class, comment) comment 엔티티만 가져오고 싶어서!
        List<Comment> result =
                queryFactory.select(Projections.fields(Comment.class, comment.id))
                        .from(comment)
//                        .leftJoin(comment.parent).fetchJoin()
                        .where(comment.board.boardNo.eq(boardNo))
                        .orderBy(comment.parent.id.asc().nullsFirst(),
                                comment.regTime.asc()
                        ).fetch();
        log.info("result = {}" , result);

    }


    @Test
    @DisplayName("querydsl- where 다중파라미터 ")
    private List<Member> searchWhere(){

        QMember member = QMember.member;
        Predicate predicate = member.userName.eq("keonjuu101@inno")
                .and(member.role.eq(Role.USER));


        return queryFactory
                .selectFrom(member)
//                .where(userNameEq("keonjuu101@inno"), roleEq(Role.USER))
//                .where(predicate)
                .fetch();
    }
/*
    private BooleanExpression userNameEq(String userNameCond){
        QMember member = QMember.member;
        return userNameCond != null? member.userName.eq(userNameCond) : null;
    };


    private BooleanExpression roleEq(Role roleCond){
        QMember member = QMember.member;
        return roleCond != null? member.role.eq(roleCond) : null;
    };
*/



}
