package com.Board.Board.dto;

import com.Board.Board.entity.Board;
import com.Board.Board.entity.BoardCategory;
import com.Board.Member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BoardForm {

    private Long boardNo;

    @NotBlank
    private String title;

    @Size
    private String content;
    private String regId;
    private String modId;
    private LocalDateTime modTime;
    private LocalDateTime regTime;

    @NotNull(message = "필수 값입니다.")
    private BoardCategory category;

    private Member member;
    private List<MultipartFile> attachFiles;

    private List<AttachFileForm> files;

    private List<CommentForm> comments;

    public static BoardForm toDTO(Board boardEntity){

        List<AttachFileForm> fileForm = boardEntity.getAttachFiles()
                .stream()
                .map(AttachFileForm::new)
                .collect(toList());

        List<CommentForm> commentForm = boardEntity.getComments()
                .stream()
                .map(comment -> CommentForm.toDTO(comment)) //.map(CommentForm::new) // comment -> new CommentForm(comment)
                .sorted(Comparator.comparing(CommentForm::getLevel)
                        .thenComparing(CommentForm::getModTime))// modTime을 기준으로 정렬
                .collect(toList());

        return BoardForm.builder()
                .boardNo(boardEntity.getBoardNo())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .modId(boardEntity.getModId())
                .modTime(boardEntity.getModTime())
                .regTime(boardEntity.getRegTime())
                .regId(boardEntity.getMember().getEmail()) // Lazy 초기화 ??? -> N + 1 문제
                .files(fileForm)
                .comments(commentForm)
                .category(boardEntity.getBoardCategory())
                .build();
    }


}
