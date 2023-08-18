package exc.Board.controller.Login;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class LoginForm {

    @NotBlank /*(message ="이메일을 입력해야 합니다.")*/
    private String email;

    @NotBlank /*(message = "비밀번호를 입력해야 합니다.")*/
    private String pwd;

}
