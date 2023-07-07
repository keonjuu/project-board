package exc.Board.controller.Board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardForm {
    private Long boardNo;
    private String title;
    private String content;
//    private String modId;
    private LocalDateTime modTime;
}
