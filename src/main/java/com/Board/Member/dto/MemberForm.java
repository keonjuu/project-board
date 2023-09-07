package com.Board.Member.dto;

import com.Board.Member.entity.Member;
import com.Board.Member.entity.MemberStatus;
import com.Board.Member.entity.Role;
import lombok.*;

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

     public MemberForm(String name, String email) {
          this.name = name;
          this.email = email;
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
