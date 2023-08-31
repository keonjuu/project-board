package com.Board.Board.dto;

import com.Board.Board.entity.Comment;
import lombok.*;


@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentRegisterForm {
    private String content;
    private Long boardNo;
    private String regId;
    private Long parentNo; // 부모 댓글 번호
    private String isDeleted;
    private Integer level;

    public CommentRegisterForm(Comment comment) {
        this.content = comment.getContent();
        this.boardNo = comment.getBoard().getBoardNo();
        this.regId = comment.getMember().getEmail();
        this.parentNo = comment.getParent().getId();
        this.isDeleted = comment.getIsDeleted();
        this.level = comment.getLevel();
    }

}
