package com.Board.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateForm {

    private Long boardNo;
    @NotBlank
    private String title;
    @Size
    private String content;
    private String modId;

    private List<MultipartFile> attachFiles;
    private List<AttachFileForm> files;
    private List<Long> deletionQueue;

}
