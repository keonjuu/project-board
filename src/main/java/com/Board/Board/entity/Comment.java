package com.Board.Board.entity;


import com.Board.Member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_no")
    private Long id;

    @Column(columnDefinition="TEXT")
    private String content;


    @JsonIgnore
    @CreatedBy
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reg_id", referencedColumnName = "email")
    private Member member;

    @LastModifiedBy
    private LocalDateTime modId;
    @CreatedDate
    private LocalDateTime regTime;
    @LastModifiedDate
    private LocalDateTime modTime;

//    @JsonBackReference
//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no", referencedColumnName = "comment_no")
    private Comment parent;

//    @JsonIgnore
    @ToString.Exclude
//    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE,  orphanRemoval = true)
    private List<Comment> childs = new ArrayList<>();

//    @JsonIgnore
//    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private Board board;


    private String isDeleted;
    private Integer level;


    // 부모-자식 관계 설정 메서드
    public void setParent(Comment comment){
        this.parent = comment;
        parent.getChilds().add(this);
    }

    // 자식과 연관관계 메서드
    public void addChild(Comment comment){
        childs.add(comment);
        comment.setParent(this);
    }
    public void removeChild(Comment comment){
        childs.remove(comment);
        comment.setParent(null); // 자식과 부모 연결 해제
    }
    //
    public void setBoard(Board board) {
        // 기존 관계 제거
        if (this.board !=null) {
            this.board.getComments().remove(this);
        }
        this.board = board;
        board.getComments().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
    }
    public Member getMember() {
        return member;
    }
}