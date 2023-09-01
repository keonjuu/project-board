package com.Board.Board.repository;

import com.Board.Board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Transactional
    @Modifying
    @Query("update Comment c set c.content =  concat(c.content, '(삭제된 댓글입니다)'), c.isDeleted = 'Y' where c.id = :id")
    int updatedeleteContentById(@Param("id") Long id);

    //1. find
    Optional<Comment> findById(Long id) ;

    @Query("select c from Comment c where c.parent.id = :id")
    List<Comment> findAllCommentByParentNo(@Param("id") Long id);


}
