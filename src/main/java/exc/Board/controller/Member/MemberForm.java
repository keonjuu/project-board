package exc.Board.controller.Member;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.domain.member.Role;
import lombok.*;

import java.time.LocalDateTime;

//@Getter
//@Setter
@Data
@Builder
@NoArgsConstructor
public class MemberForm {
//     @NotBlank(message ="이름을 입력해야 합니다.")
     private String name;

//     @NotBlank(message ="이메일을 입력해야 합니다.")
     private String email;

//     @NotBlank(message ="비밀번호를 입력해야 합니다")
     private String pwd;

     public MemberForm(String name, String email, String pwd) {
          this.name = name;
          this.email = email;
          this.pwd = pwd;
     }

     public static Member toEntity(MemberForm form) {
          return Member.builder()
                  .email(form.getEmail())
                  .userName(form.getName())
                  .password(form.getPwd())
                  .role(Role.USER)
                  .status(MemberStatus.PENDING)
                  .build();
     }

}
