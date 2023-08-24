package com.Board.Board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class AttachFile {
    @Id
    @GeneratedValue
    @Column(name = "fileNo")
    private Long id;

    private String originalFileName;
    private String storeFileName;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardNo" , referencedColumnName = "boardNo")
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

    @CreatedDate
    private LocalDateTime regTime;

    @LastModifiedDate
    private LocalDateTime modTime;

}