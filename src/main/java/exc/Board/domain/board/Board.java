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

import static exc.Board.domain.board.BoardCategory.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder(toBuilder = true)
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "regId", referencedColumnName = "email")
    private Member member;

    public Board() {}


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
    @LastModifiedDate
//    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime modTime;

//    @LastModifiedBy
    private String modId;

    @Column(columnDefinition = "varchar(1) default 'N'")
    @Default private String delYn = "N";


}




