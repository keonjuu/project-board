package exc.Board.service;


import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import exc.Board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    //1. 저장
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    //2.게시판 전체 조회
    public Page<Board> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findAll(pageable);
    }

    //3. 게시판 상세 조회
    public Board findOne(Long boardNo) {
        return boardRepository.findByBoardNo(boardNo);
    }


    //4. 게시판 카테고리 조회
    public Page<Board> findCategory(BoardCategory category, Pageable pageable) {

        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findCategory(category, pageable);
    }


    //5. 게시판 조회
    public Page<Board> searchBoard(String searchType, String keyword, Pageable pageable) {

//        Page<Board> regIdPage = null;
        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "boardNo"));

        if(searchType.equals("regId")){
            return boardRepository.findByRegIdContaining(keyword, pageable);
        }
        if(searchType.equals("title")){
            return boardRepository.findByTitleContaining(keyword, pageable);
        }
        if(searchType.equals("content")){
            return boardRepository.findByContentContaining(keyword, pageable);
        }
        //System.out.println("pageable = " + pageable.toString());
        return null;
    }

}