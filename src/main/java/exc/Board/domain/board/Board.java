package exc.Board.domain.board;

import exc.Board.controller.Board.BoardForm;
import exc.Board.domain.member.Member;
import lombok.*;
import lombok.Builder.Default;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static exc.Board.domain.board.BoardCategory.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public
class Board {

    @Id @GeneratedValue
    private Long boardNo;

    @Enumerated(EnumType.STRING)
    @Default private BoardCategory boardCategory = FREE;
//    private BoardCategory boardCategory;

    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @CreatedDate
//    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime regTime;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "regId", referencedColumnName = "email")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)  //게시판 엔티티를 삭제하면 연관된 모든 첨부파일 엔티티도 삭제되도록
    private List<AttachFile> attachFiles;

    /*연관관계 매서드*/
    public void setMember(Member member){
        // 기존 member 관계 제거
        if (this.member !=null) {
            this.member.getBoardList().remove(this);
        }
        this.member = member;
        member.getBoardList().add(this);
//        log.info("board.setMember(loginMember) = {}", member);
    }

    // AttachFile과 연관관계 메서드
    public void addAttachFile(AttachFile attachFile) {
        attachFiles.add(attachFile);
        attachFile.setBoard(this);
    }

    public void removeAttachFile(AttachFile attachFile) {
        attachFiles.remove(attachFile);
        attachFile.setBoard(null);
    }


    @LastModifiedDate
//    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime modTime;

//    @LastModifiedBy
    private String modId;

    @Column(columnDefinition = "varchar(1) default 'N'")
    @Default private String delYn = "N";


}




