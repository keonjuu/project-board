package com.Board.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {
    private Long boardNo;  // 게시판 번호
    private String title;   // 제목
    private String content; // 내용
    private String regId;   // 글쓴이




}
