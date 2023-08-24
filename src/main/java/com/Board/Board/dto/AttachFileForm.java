package com.Board.Board.dto;

import com.Board.Board.entity.AttachFile;
import lombok.Data;

@Data
public class AttachFileForm {
    private Long id;
    private String originalFileName;

    public AttachFileForm(AttachFile file) {
        id = file.getId();
        originalFileName = file.getOriginalFileName();
    }
}

