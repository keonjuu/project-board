package com.Board.Board.dto;

import com.Board.Board.entity.Comment;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class CommentForm {
    private Long id;
    private String content;
    private Long boardNo;
    private String regId;
    private LocalDateTime modTime;
    private Long parentNo;
    private List<CommentForm> children;
    private String isDeleted;
    private Integer level;


    public static CommentForm toDTO(Comment commentEntity){

        List<CommentForm> childrenForm = commentEntity.getChildren()
                .stream()
                .filter(Objects::nonNull) // null 이 아닌 자식댓글만
                .map(child -> toDTO(child))
                .collect(Collectors.toList());

        return CommentForm.builder()
                    .id(commentEntity.getId())
                    .content(commentEntity.getContent())
                    .boardNo(commentEntity.getBoard().getBoardNo())
                    .regId(commentEntity.getMember().getEmail())
                    .modTime(commentEntity.getModTime())
                    .parentNo(commentEntity.getParent() != null? commentEntity.getParent().getId() : null) // parent null 일 수 있어
                    .children(commentEntity.getChildren() != null? childrenForm: null) // children null 일 수 있어
                    .isDeleted(commentEntity.getIsDeleted())
                    .level(commentEntity.getLevel())
                    .build();
    }


}
