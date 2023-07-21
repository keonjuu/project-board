package exc.Board.repository;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    @Test
    @Transactional
    @Commit
    public void 게시글_저장(){
        //given
        Board board = new Board();
        board.setTitle("안녕하세요~~");
        board.setContent("회원가입 후 두번째 게시글입니다.");
        board.setBoardCategory(BoardCategory.notice);
        board.setRegId("kkk@naver.com");
        board.setModId("kkk@naver.com");
        board.setRegTime(LocalDateTime.now());
        board.setModTime(LocalDateTime.now());

        System.out.println("board = " + board);
        //when
        boardRepository.save(board);
    }

    // 게시글 목록 조회
    @Test
    @Transactional
    public void 게시글_전체조회(){
        //when
        //given
        List<Board> boardList = boardRepository.findAll();
        //then
        for (Board board : boardList) {
            System.out.println("board = " + board.toString());
//            System.out.println("board.getTitle() = " + board.getTitle());
        }
        Assertions.assertThat(boardList.size()).isEqualTo(2);
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
        Board board = boardRepository.findOne(2L);
        //then
        Assertions.assertThat(board.getBoardNo()).isEqualTo(2L);
        Assertions.assertThat(board.getRegId()).isEqualTo("keon37");

    }

    @Test
    public void 게시글_카테고리조회(){
        List<Board> boardList = boardRepository.findAll()
                .stream()
                .filter(c -> c.getBoardCategory().equals(BoardCategory.qna))
                .sorted(Comparator.comparing(Board::getBoardNo).reversed())
                .collect(Collectors.toList());

        System.out.println("boardList = " + boardList);

        // then
        Assertions.assertThat(boardList.size()).isEqualTo(2);
        Assertions.assertThat(boardList.get(0).getBoardCategory()).isEqualTo(BoardCategory.qna);

    }
}