package com.Board.Board;

import com.Board.Board.entity.AttachFile;

import java.util.List;
import java.util.Optional;

public interface AttachFileService {
    void save(List<AttachFile> attachFiles);
    Optional<AttachFile> findAttachFileById(Long fileId);
    void deleteAttachFileById(Long fileId);
    List<AttachFile> findAttachFilesByBoardNo(Long boardNo);
}
