package exc.Board.domain.member;

import exc.Board.domain.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private MemberGroup memberGroup ;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", memberGroup=" + memberGroup +
                '}';
    }
}
