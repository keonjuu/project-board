package exc.Board.controller.Board;

import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import exc.Board.domain.member.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
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

    public static BoardForm toDTO(Board boardEntity){
        return BoardForm.builder()
                .boardNo(boardEntity.getBoardNo())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .modId(boardEntity.getModId())
                .modTime(boardEntity.getModTime())
                .regTime(boardEntity.getRegTime())
                .regId(boardEntity.getMember().getEmail()) // Lazy 초기화 ??? -> N + 1 문제
                .category(boardEntity.getBoardCategory())
                .build();
    }


}
