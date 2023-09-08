package com.Board.Board.repository;

import com.Board.Board.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.Board.Board.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findByCommentId(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(comment)
                .where(comment.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<Comment> findAllByBoardNoWithParent(Long boardNo) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.board.boardNo.eq(boardNo))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.regTime.asc()
                ).fetch();
    }

}
