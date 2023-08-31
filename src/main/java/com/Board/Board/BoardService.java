package com.Board.Board;


import com.Board.Board.dto.BoardForm;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.BoardCategory;
import com.Board.Board.entity.AttachFile;
import com.Board.Board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    // 삭제
    @Transactional
    public void deleteById(Long boardNo){
        boardRepository.deleteById(boardNo);
    }

    //1. 저장
    @Transactional
    public void save(Board board) {
        List<AttachFile> storeFiles = board.getAttachFiles();

        // AttachFile 엔티티의 board 연관관계 설정
        List<AttachFile> updatedStoreFiles = new ArrayList<>(storeFiles);
        for (AttachFile attachFile : updatedStoreFiles) {
            attachFile.setBoard(board);
//            log.info("getStoreFileName= {}" ,attachFile.getStoreFileName());
        }

/*        List<AttachFile> updatedStoreFiles = new ArrayList<>();
        storeFiles.forEach(s -> {
            AttachFile.AttachFileBuilder fileBuilder = s.toBuilder().board(board);
            updatedStoreFiles.add(fileBuilder.build());
        });

        log.info("storeFiles = {}", updatedStoreFiles);*/
        boardRepository.save(board);
    }

        //2.게시판 전체 조회
        public Page<BoardForm> findAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findAll(pageable)
                .map(board -> BoardForm.builder()
                        .boardNo(board.getBoardNo())
                        .title(board.getTitle())
                        .regId(board.getMember().getEmail())
//                        .member(board.getMember())
                        .build()
                );
    }

    //3. 게시판 상세 조회
    public Board findOne(Long boardNo) {
        return boardRepository.findByBoardNo(boardNo);
    }


    //4. 게시판 카테고리 조회
    public Page<BoardForm> findCategory(BoardCategory category, Pageable pageable) {

        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findCategory(category, pageable)
                .map(board -> BoardForm.builder()
                        .boardNo(board.getBoardNo())
                        .title(board.getTitle())
                        .regId(board.getMember().getEmail())
//                        .member(board.getMember())
                        .build()
                );
    }


    //5. 게시판 조회
    public Page<BoardForm> searchBoard(String searchType, String keyword, Pageable pageable) {

//        Page<Board> regIdPage = null;
        int pageNumber = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "boardNo"));

        if(searchType.equals("regId")){
            return boardRepository.findByRegIdContaining(keyword, pageable).map(board -> BoardForm.toDTO(board));
        }
        if(searchType.equals("title")){
            return boardRepository.findByTitleContaining(keyword, pageable).map(board -> BoardForm.toDTO(board));
        }
        if(searchType.equals("content")){
            return boardRepository.findByContentContaining(keyword, pageable).map(board -> BoardForm.toDTO(board));
        }
//        log.info("pageable = {}" ,pageable.toString());
        return null;
    }

}