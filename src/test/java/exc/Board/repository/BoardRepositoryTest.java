package exc.Board.repository;

import exc.Board.controller.Board.BoardForm;
import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import exc.Board.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;
    private static Logger logger = LoggerFactory.getLogger(BoardRepositoryTest.class);

/*    @Test
    @Transactional
    @Commit
    public void 게시글_저장(){
        //given
        Board board = new Board();
        board.setTitle("안녕하세요~~");
        board.setContent("회원가입 후 열두번째 공지글입니다.");
        board.setBoardCategory(BoardCategory.NOTICE);
//        board.setMember(new Member("kkk@naver.com"));
//        board.setRegId("kkk@naver.com");
        board.setModId("kkk@naver.com");
        board.setRegTime(LocalDateTime.now());
        board.setModTime(LocalDateTime.now());

        System.out.println("board = " + board);
        //when
        boardRepository.save(board);
    }*/

    // 게시글 목록 조회
    @Test
    @Transactional
    public void 게시글_전체조회(){
        //when
        //given
        List<Board> boards = boardRepository.findAll();
        //then
        for (Board board : boards) {
            System.out.println("board = " + board.toString());
//            System.out.println("board.getTitle() = " + board.getTitle());
        }
        Assertions.assertThat(boards.size()).isEqualTo(2);
    }

    // 게시글 상세조회
    @Test
    public void 게시글_상세조회(){
        //when
/*
        Board board = new Board();
        board.setBoardNo(2L);
*/
        //given
        Board board = boardRepository.findByBoardNo(2L);
        //then
        Assertions.assertThat(board.getBoardNo()).isEqualTo(2L);
//        Assertions.assertThat(board.getRegId()).isEqualTo("keon37");

    }

    //3. 게시판 상세 조회
    @Test
    public void findOne() {

        Long boardNo = 13L;
        Board byBoardNo = boardRepository.findByBoardNo(boardNo);
        logger.info("toDTO = {}", BoardForm.toDTO(byBoardNo) );
//        return Board.toDTO(byBoardNo);
    }



    @Test
    public void 게시글_카테고리조회(){
        List<Board> boards = boardRepository.findAll()
                .stream()
                .filter(c -> c.getBoardCategory().equals(BoardCategory.QNA))
                .sorted(Comparator.comparing(Board::getBoardNo).reversed())
                .collect(Collectors.toList());

        System.out.println("boards = " + boards);

        // then
        Assertions.assertThat(boards.size()).isEqualTo(2);
        Assertions.assertThat(boards.get(0).getBoardCategory()).isEqualTo(BoardCategory.QNA);

    }

    @Test
    @Transactional
    public void 게시글_전체조회_페이징() throws Exception {
        //given

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<Board> page = boardRepository.findAll(pageRequest);
//        Slice<Member> slice = memberRepository.findAll(pageRequest);

        //then
        List<Board> boards = page.getContent();
        System.out.println("boards size = " + boards.size());
        for (Board board : boards) {
            System.out.println("board = " + board);
        }
    }

    @Test
    public void 게시글_카테고리조회_페이징(){

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> page = boardRepository.findCategory(BoardCategory.NOTICE,pageRequest);

        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.NOTICE)).collect(Collectors.toList()));
/*        List<Board> boards = boardRepository.findAll(pageRequest).stream()
                .filter(c -> c.getBoardCategory().equals(BoardCategory.nO)).collect(Collectors.toList());


//        System.out.println("boards = " + boards);
        Page<Board> boards = new PageImpl<>(boards);
        System.out.println("boards = " + boards);
/*
        // then
        Assertions.assertThat(boards.size()).isEqualTo(2);
        Assertions.assertThat(boards.get(0).getBoardCategory()).isEqualTo(BoardCategory.QNA);
*/

    }

    @Test
    public void 글쓴이검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        String keword = "keonjuu";
        Page<Board> page = boardRepository.findByRegIdContaining(keword, pageRequest);
        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.QNA)).collect(Collectors.toList()));

    }


    @Test
    public void 타이틀검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));

        String keword = "언제쯤";
        Page<Board> page = boardRepository.findByTitleContaining(keword, pageRequest);
        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.QNA)).collect(Collectors.toList()));

    }

    @Test
    public void 콘텐츠검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));
        String keword = "언제쯤";
        Page<Board> page = boardRepository.findByContentContaining(keword, pageRequest);
        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.QNA)).collect(Collectors.toList()));

    }



    @Test
    public void 카테고리별_타이틀검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));
        String keword = "언제쯤";
        Page<Board> page = boardRepository.findByBoardCategoryAndDelYnEqualsAndTitleContaining(BoardCategory.QNA,"N", keword ,pageRequest);
        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.QNA)).collect(Collectors.toList()));

    }
    @Test
    public void 카테고리별_내용검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));
        String keword = "언제쯤";
        Page<Board> page = boardRepository.findByBoardCategoryAndDelYnEqualsAndContentContaining(BoardCategory.QNA, "N",keword ,pageRequest);
        System.out.println("page.getContent = " + page.getContent());
        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.QNA)).collect(Collectors.toList()));

    }

    @Test
    public void 카테고리별_타이틀내용검색(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));
        String keword = "재택";
        Page<Board> qnaPage = boardRepository.findTitleOrContentContaining(BoardCategory.QNA, keword, pageRequest);
        Page<Board> noticePage = boardRepository.findTitleOrContentContaining(BoardCategory.NOTICE, keword, pageRequest);
        Page<Board> freePage = boardRepository.findTitleOrContentContaining(BoardCategory.FREE, keword, pageRequest);
        System.out.println("qnaPage.getContent = " + qnaPage.getContent());
        System.out.println("noticePage.getContent = " + noticePage.getContent());
        System.out.println("freePage.getContent = " + freePage.getContent());

//        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getBoardCategory().equals(BoardCategory.NOTICE)).collect(Collectors.toList()));

    }

    @Test
    public void 페이징사용자글목록조회() {

        //when
        Long findId = 20230006L;
        Member findMember = memberRepository.find(findId);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "modTime"));
        Page<Board> userPage = boardRepository.findAllByUserNo(findMember.getId(), pageRequest);

        //then
        List<Board> boards = userPage.getContent();
        System.out.println("page.getContent() = " + userPage.getContent());
        System.out.println("page.getSize() = " + userPage.getSize());  // 페이지 크기
        System.out.println("page.getTotalElements = " + userPage.getTotalElements()); // 전체 count
        System.out.println("boards size = " + boards.size());
/*        for (Board board : boards) {
            System.out.println("board = " + board);
        }*/

    }


    @Test
    public void 게시글_카테고리조회_페이징_DTO() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<BoardForm> page = boardRepository.findCategory(BoardCategory.NOTICE, pageRequest).map(board -> Board.toDTO(board));
        Page<BoardForm> page = boardRepository.findCategory(BoardCategory.NOTICE, pageRequest)
                        .map(board -> BoardForm.builder()
                                        .title(board.getTitle())
                                        .regId(board.getMember().getEmail())
                                        .build()
                             );


        System.out.println("page.getContent = " + page.getContent());
//        System.out.println("boards = " + page.getContent().stream().filter(board -> board.getCategory().equals(BoardCategory.NOTICE)).collect(Collectors.toList()));

    }



}