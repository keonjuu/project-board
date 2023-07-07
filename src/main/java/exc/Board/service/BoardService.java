package exc.Board.service;


import exc.Board.domain.Board;
import exc.Board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    //1. 저장
    @Transactional
    public void save(Board board){
         boardRepository.save(board);
    }

    //2.게시판 전체 조회
    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    //3. 게시판 상세 조회
    public Board findOne(Long boardNo){return boardRepository.findOne(boardNo);}

}
