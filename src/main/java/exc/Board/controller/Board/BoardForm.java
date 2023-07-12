package exc.Board.controller.Board;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardForm {
    private Long boardNo;

    @NotBlank
    private String title;

    @Size
    private String content;
//    private String modId;
    private LocalDateTime modTime;
}
