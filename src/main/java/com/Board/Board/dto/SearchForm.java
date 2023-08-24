package com.Board.Board.dto;

import com.Board.Board.entity.BoardCategory;
import lombok.Getter;

@Getter
public class SearchForm {
//    private String writer;
    private String title;
    private String content;

    // 검색 필터
    private BoardCategory category;
    private String searchType;
    private String keyword;

}
