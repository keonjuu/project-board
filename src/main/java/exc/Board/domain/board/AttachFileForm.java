package exc.Board.domain.board;

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

