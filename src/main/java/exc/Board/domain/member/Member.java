package exc.Board.domain.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "member_seq",
        allocationSize = 1
)
public class Member {

    @Id /*@GeneratedValue(generator = "sequence-generator")*/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @Column(name = "userNo")
    private Long id;

    private String email;

    private String userName;

    private String password;

/*
    @Embedded
    private DateEntity dateEntity;
*/

/*    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();;*/

    @Enumerated(EnumType.STRING)
    private MemberType memberType=MemberType.USER;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.PENDING;

    @Column(columnDefinition="DATETIME(0) default CURRENT_TIMESTAMP")
    private LocalDateTime lastDatetime;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", memberType=" + memberType +
                '}';
    }
}
