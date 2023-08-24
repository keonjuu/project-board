package com.Board.service;

import com.Board.Board.BoardService;
import com.Board.Board.dto.BoardForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

/*
    @Test
    @Transactional
    @Commit
    public void 저장(){
        Board board = new Board();
        board.setTitle("테스트");
        board.setContent("tttttttt");
        boardService.save(board);
    }
*/

    @Test
    @DisplayName("검색조회")
    public void search(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Board> searchList = boardService.searchBoard("title", "공지사항", pageRequest);
        Page<BoardForm> searchList = boardService.searchBoard("regId", "keonjuu", pageRequest);

//        Page<Board> searchList = boardService.searchBoard("title", notice,"공지사항",pageRequest);
        log.info("size = {}" , searchList.getContent().size());
        log.info("getContent = {}" , searchList.getContent());
    }

}