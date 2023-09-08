package com.Board.Board.repository;

import com.Board.Board.entity.Comment;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepositoryCustom {

    Optional<Comment> findByCommentId(Long id);
    List<Comment> findAllByBoardNoWithParent(@Param("id") Long id);
}
