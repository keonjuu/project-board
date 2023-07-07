package exc.Board.controller.Login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class loginForm {

    @NotBlank /*(message ="이메일을 입력해야 합니다.")*/
    private String email;

    @NotBlank /*(message = "비밀번호를 입력해야 합니다.")*/
    private String pwd;
}
