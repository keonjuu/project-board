package com.Board.Board.dto;

import com.Board.Board.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentForm {
    private Long id;
    private String content;
    private Long boardNo;
    private String regId;
    private LocalDateTime modTime;
    private Long parentNo;
    private List<CommentForm> childs;
    private String isDeleted;
    private Integer level;


    public static CommentForm toDTO(Comment commentEntity){

        List<CommentForm> childsForm = commentEntity.getChilds()
                .stream()
                .filter(Objects::nonNull) // null 이 아닌 자식댓글만
                .map(child -> toDTO(child))
//                .sorted(Comparator.comparing(CommentForm::getModTime))  // 정렬
                .collect(toList());

        return CommentForm.builder()
                    .id(commentEntity.getId())
                    .content(commentEntity.getContent())
                    .boardNo(commentEntity.getBoard().getBoardNo())
                    .regId(commentEntity.getMember().getEmail())
                    .modTime(commentEntity.getModTime())
                    .parentNo(commentEntity.getParent() != null? commentEntity.getParent().getId() : null) // parent null 일 수 있어
                    .childs(commentEntity.getChilds() != null? childsForm: null) // childs null 일 수 있어
                    .isDeleted(commentEntity.getIsDeleted())
                    .level(commentEntity.getLevel())
                    .build();
    }

/*    public CommentForm(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.boardNo = comment.getBoard().getBoardNo();
        this.regId = comment.getMember().getEmail();
        this.modTime = comment.getModTime();
        if (comment.getParent() != null) {
            this.parentNo = comment.getParent().getId();
        }
        this.isDeleted = comment.getIsDeleted();

    }*/

}
