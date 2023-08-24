package com.Board.Board;

import com.Board.Board.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    List<AttachFile> findByBoard_BoardNo(Long boardNo);

}