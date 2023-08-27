package com.Board.Board.dto;

import com.Board.Board.entity.AttachFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileForm {
    private Long id;
    private String originalFileName;

    public AttachFileForm(AttachFile file) {
        id = file.getId();
        originalFileName = file.getOriginalFileName();
    }
}

