package com.Board.Board;

import com.Board.Board.dto.BoardForm;
import com.Board.Board.dto.SearchForm;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    void deleteById(Long boardNo);
    void save(Board board);
    Page<BoardForm> findAll(Pageable pageable);
    Board findOne(Long boardNo);
    Page<BoardForm> findCategory(BoardCategory category, Pageable pageable);
    Page<BoardForm> searchBoard(String searchType, String keyword, Pageable pageable);
    Page<BoardForm> searchBoardQuerydsl(SearchForm searchForm, Pageable pageable);

}

