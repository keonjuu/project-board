package exc.Board.controller.Board;

import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import exc.Board.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import org.hibernate.event.spi.PostUpdateEventListener;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BoardForm {
    private Long boardNo;

    @NotBlank
    private String title;

    @Size
    private String content;
    private String regId;
    private String modId;
    private LocalDateTime modTime;
    private LocalDateTime regTime;

    @NotNull(message = "필수 값입니다.")
    private BoardCategory category;

    private Member member;

    public BoardForm(){}

    public BoardForm(Long boardNo, String title, String content,String regId,String modId, LocalDateTime modTime,LocalDateTime regTime, BoardCategory category, Member member) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.regId = regId;
        this.modId = modId;
        this.modTime = modTime;
        this.regTime = regTime;
        this.category = category;
        this.member = member;
    }

}
