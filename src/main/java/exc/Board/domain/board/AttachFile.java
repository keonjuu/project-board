package exc.Board.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;


@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class AttachFile {
    @Id
    @GeneratedValue
    @Column(name = "fileNo")
    private Long id;

    private String originalFileName;
    private String storeFileName;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "board_no" , referencedColumnName = "boardNo")
    @ToString.Exclude
    @JsonIgnore
    private Board board;

    // Board 와 연관관계 메서드

    public void setBoard(Board board) {
        if (this.board != null) {
            this.board.getAttachFiles().remove(this);
        }
        this.board = board;
        if (board != null) {
            board.getAttachFiles().add(this);
        }
    }

/*    public AttachFile(AttachFile attachFile){
        filePath = attachFile.filePath;
        originalFileName = attachFile.originalFileName;
        storeFileName = attachFile.storeFileName;
    }*/

}