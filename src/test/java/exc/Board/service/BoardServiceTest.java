package exc.Board.service;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static exc.Board.domain.BoardCategory.notice;
import static exc.Board.domain.BoardCategory.qna;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired BoardService boardService;
    private Pageable pageable;

    @Test
    @Transactional
    @Commit
    public void 저장(){
        Board board = new Board();
        board.setTitle("테스트");
        board.setContent("tttttttt");
        boardService.save(board);
    }

/*
    @Test
    public void 검색조회(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "boardNo"));
        Page<Board> searchList = boardService.searchBoard("title", notice,"공지사항",pageRequest);
//        Page<Board> searchList = boardService.searchBoard("title", notice,"공지사항",pageRequest);
        System.out.println("getContent = " + searchList.getContent().size());
        System.out.println("getContent = " + searchList.getContent());
    }
*/


    @Test
    public void 검색조회(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Board> searchList = boardService.searchBoard("title", "공지사항", pageRequest);
        Page<Board> searchList = boardService.searchBoard("regId", "keonjuu", pageRequest);

//        Page<Board> searchList = boardService.searchBoard("title", notice,"공지사항",pageRequest);
        System.out.println("size = " + searchList.getContent().size());
        System.out.println("getContent = " + searchList.getContent());
    }

}