package com.Board.Board.entity;

import com.Board.Member.entity.Member;
import lombok.*;
import lombok.Builder.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.Board.Board.entity.BoardCategory.FREE;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public
class Board {

    @Id
    @GeneratedValue
    private Long boardNo;

    @Enumerated(EnumType.STRING)
    @Default private BoardCategory boardCategory = FREE;

    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime regTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reg_id", referencedColumnName = "email")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,  orphanRemoval = true)  //게시판 엔티티를 삭제하면 연관된 모든 첨부파일 엔티티도 삭제되도록
    private List<AttachFile> attachFiles;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();


    /*연관관계 매서드*/
    public void setMember(Member member){
        // 기존 member 관계 제거
        if (this.member !=null) {
            this.member.getBoards().remove(this);
        }
        this.member = member;
        member.getBoards().add(this);
        //log.info("board.setMember(loginMember) = {}", member);
    }

    // AttachFile 과 연관관계 메서드
    public void addAttachFile(AttachFile attachFile) {
        attachFiles.add(attachFile);
        attachFile.setBoard(this);
    }

    public void removeAttachFile(AttachFile attachFile) {
        attachFiles.remove(attachFile);
        attachFile.setBoard(null);
    }
    // Comment 과 연관관계 메서드
    public void addComment(Comment comment){
        comments.add(comment);
        comment.setBoard(this);
    }
    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setBoard(null);
    }


    @LastModifiedDate
    private LocalDateTime modTime;

//    @LastModifiedBy
    private String modId;

    @Column(columnDefinition = "varchar(1) default 'N'")
    @Default private String delYn = "N";


}




