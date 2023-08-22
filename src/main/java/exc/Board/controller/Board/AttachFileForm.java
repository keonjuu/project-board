package exc.Board.controller.Board;

import exc.Board.domain.board.AttachFile;
import lombok.Data;

@Data
public class AttachFileForm {
    private Long id;
    private String originalFileName;

    public AttachFileForm(AttachFile file) {
        id = file.getId();
        originalFileName = file.getOriginalFileName();
    }
}

