package exc.Board.domain;

import exc.Board.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import java.time.LocalDateTime;

import static exc.Board.domain.BoardCategory.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Board {

    @Id @GeneratedValue
    private Long boardNo;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory = normal;

    private String title;
    private String content;

    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime regTime;

    private String regId;

    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime modTime;

    private String modId;

    @Override
    public String toString() {
        return "Board{" +
                "boardNo=" + boardNo +
                ", boardCategory=" + boardCategory +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", regTime=" + regTime +
                ", regId='" + regId + '\'' +
                ", modTime=" + modTime +
                ", modId='" + modId + '\'' +
                '}';
    }


    /*
        @Embedded
        private DateEntity dateEntity;
    */
/*
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reg_Id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mod_Id")
    private Member member;


    // 연관관계 편의 메서드 정의하기
    public void setMembers(Member member){
        // 기존 Member관계 제거
        if (this.member !=null){
            this.member.getBoardList().remove(this);
        }
        this.member = member;
        member.getBoardList().add(this);
    }
*/

/*
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "register_Id")
*/

}
