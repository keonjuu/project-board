package exc.Board.domain.board;

import exc.Board.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static exc.Board.domain.board.BoardCategory.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Board {

    @Id @GeneratedValue
    private Long boardNo;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory = free;

    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime regTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "regId", referencedColumnName = "email")
    private Member member;
//    private String regId;

    /*연관관계 매서드*/
    public void setMember(Member member){
        // 기존 member 관계 제거
        if (this.member !=null) {
            this.member.getBoardList().remove(this);
        }
        this.member = member;
        member.getBoardList().add(this);


//        System.out.println("board.setMember(loginMember) = " + member);
    }

    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime modTime;

    private String modId;

    @Column(columnDefinition = "varchar(1) default 'N'")
    private String delYn = "N";


    @Override
    public String toString() {
        return "Board{" +
                "boardNo=" + boardNo +
                ", boardCategory=" + boardCategory +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", regTime=" + regTime +
                ", modTime=" + modTime +
                ", modId='" + modId + '\'' +
                ", delYn='" + delYn + '\'' +
                '}';
    }
}



