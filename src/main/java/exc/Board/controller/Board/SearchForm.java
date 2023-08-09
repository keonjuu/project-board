package exc.Board.controller.Board;

import exc.Board.domain.BoardCategory;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

import static exc.Board.domain.BoardCategory.free;

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
