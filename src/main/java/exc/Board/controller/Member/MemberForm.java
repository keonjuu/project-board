package exc.Board.controller.Member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

//@Getter
//@Setter
@Data
public class MemberForm {
//     @NotBlank(message ="이름을 입력해야 합니다.")
     private String name;

//     @NotBlank(message ="이메일을 입력해야 합니다.")
     private String email;

//     @NotBlank(message ="비밀번호를 입력해야 합니다")
     private String pwd;

}
