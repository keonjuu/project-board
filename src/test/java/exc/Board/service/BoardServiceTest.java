package exc.Board.service;

import exc.Board.domain.Board;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired BoardService boardService;

    @Test
    @Transactional
    @Commit
    public void 저장(){
        Board board = new Board();
        board.setTitle("테스트");
        board.setContent("tttttttt");
        boardService.save(board);
    }

}