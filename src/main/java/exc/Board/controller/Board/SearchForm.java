package exc.Board.controller.Board;

import exc.Board.domain.board.BoardCategory;
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
