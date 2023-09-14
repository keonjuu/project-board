package com.Board.Board;

import com.Board.Board.dto.CommentRegisterForm;
import com.Board.Board.entity.Comment;

import java.util.List;

public interface CommentService {
    void save(CommentRegisterForm form);
    Comment findById(Long commentNo);
    void deleteByComment(Long commnetNo);
    Comment searchDeletableComment(Comment comment);
    List<Comment> findAll(Long boardNo);
    List<Comment> createHierarchy(List<Comment> allComments);

}
