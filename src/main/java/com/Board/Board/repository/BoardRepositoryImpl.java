package com.Board.Board.repository;

import com.Board.Board.dto.SearchForm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.Board.Board.entity.QBoard.board;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    // 5- 검색 Querydsl 동적쿼리 - where 다중 파라미터
    @Override
    public Page<SearchForm> search(SearchForm search, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (hasText(search.getTitle()) ) {
            whereClause.and(titleContain(search.getTitle()));
        }
        if (hasText(search.getContent()) ) {
            whereClause.and(contentContain(search.getContent()));
        }
        if (hasText(search.getRegId()) ) {
            whereClause.and(regIdContain(search.getRegId()));
        }

        List<SearchForm> boards = queryFactory
                .select(Projections.constructor(
                        SearchForm.class,
                        board.boardNo,
                        board.title,
                        board.content,
                        board.member.email.as("regId"))
                ).from(board)
                .where(board.delYn.eq("N").and(whereClause))
                .offset(pageable.getOffset())   // (1) 페이지 번호
                .limit(pageable.getPageSize())  // (2) 페이지 사이즈
                .fetch();

        long total = queryFactory
                .selectFrom(board)
                .where(board.delYn.eq("N").and(whereClause))
                .fetchCount();

//        return new PageImpl<>(boards, pageable, total);
        // Count 쿼리 최적화 - 내부적으로 Count 쿼리가 필요없으면 조회 X
        return PageableExecutionUtils.getPage(boards, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("boardNo")), () -> total);

    }

    // BooleanExpression  - where
    private BooleanExpression titleContain(String titleCond){
        return titleCond != null? board.title.contains(titleCond) : null;
    };
    private BooleanExpression contentContain(String contentCond){
        return contentCond != null? board.content.contains(contentCond) : null;
    };
    private BooleanExpression regIdContain(String regIdCond){
        return regIdCond != null? board.member.email.contains(regIdCond) : null;
    };


}