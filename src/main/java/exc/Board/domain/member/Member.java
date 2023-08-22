package exc.Board.domain.member;

import exc.Board.controller.Member.MemberForm;
import exc.Board.domain.board.Board;
import lombok.*;
import lombok.Builder.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@ToString(exclude = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Slf4j
@SequenceGenerator(
    name = "SEQ_GENERATOR",
    sequenceName = "member_seq",
    allocationSize = 1
)
public class Member implements Serializable {

    @Id /*@GeneratedValue(generator = "sequence-generator")*/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @Column(name = "user_no")
    private Long id;
    private String email;
    private String userName;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Default private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Default private MemberStatus status = MemberStatus.PENDING;

    //@Column(columnDefinition = "DATETIME(0) default CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime lastDatetime;

    public Member(String name, String email, String pwd, Role role, MemberStatus status, LocalDateTime lastDatetime) {
        this.userName = name;
        this.email = email;
        this.password = pwd;
        this.role = role;
        this.status = status;
        this.lastDatetime = lastDatetime;
    }

    public static MemberForm toMemberFormDTO(Member entity){
        return MemberForm.builder()
                .name(entity.getUserName())
                .email(entity.getEmail())
                .pwd(entity.getPassword())
                .build();
    }


}