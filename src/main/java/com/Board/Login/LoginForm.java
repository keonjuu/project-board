package com.Board.Login;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class LoginForm {

    @NotBlank /*(message ="이메일을 입력해야 합니다.")*/
    private String email;

    @NotBlank /*(message = "비밀번호를 입력해야 합니다.")*/
    private String pwd;

}
