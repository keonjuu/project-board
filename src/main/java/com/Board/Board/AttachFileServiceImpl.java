package com.Board.Board;

import com.Board.Board.entity.AttachFile;
import com.Board.Board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttachFileServiceImpl implements AttachFileService{

    private final AttachFileRepository attachFileRepository;
    @Override
    @Transactional
    public void save(List<AttachFile> attachFiles) {
        for (AttachFile attachFile : attachFiles) {
            attachFileRepository.save(attachFile);
        }
    }

    @Override
    public Optional<AttachFile> findAttachFileById(Long fileId) {
        return attachFileRepository.findById(fileId);
    }

    @Override
    @Transactional
    public void deleteAttachFileById(Long fileId){
        attachFileRepository.deleteById(fileId);
    }

    @Override
    public List<AttachFile> findAttachFilesByBoardNo(Long boardNo){
        return attachFileRepository.findByBoard_BoardNo(boardNo);
    }


}
