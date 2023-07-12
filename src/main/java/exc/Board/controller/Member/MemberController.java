package exc.Board.controller.Member;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberType;
import exc.Board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("members/new")
    public String createMemberFrom(Model model) {
//        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("memberForm", new MemberForm());
        return "Member/createMemberForm";
    }

    @PostMapping("members/new")
    public String join(@ModelAttribute("memberForm") @Valid MemberForm form, BindingResult bindingResult) {
        if(form.getName().isEmpty()){
            bindingResult.rejectValue("name","name","이름을 반드시 입력해야 합니다.");
        }
        if(form.getEmail().isEmpty()){
            bindingResult.rejectValue("email","email","이메일을 반드시 입력해야 합니다.");
//            bindingResult.addError(new FieldError("form","email","이메일을 반드시 입력해야 합니다."));
        }
        if(form.getPwd().isEmpty()){
            bindingResult.rejectValue("pwd","pwd","비밀번호를 반드시 입력해야 합니다.");
//            bindingResult.addError(new FieldError("form","email","비밀번호를 반드시 입력해야 합니다."));
        }
        if(bindingResult.hasErrors()){
            log.info("errors = {}" ,bindingResult.toString());
            // bindingResult 를 다시 model 에 안담아도 돼! 자동으로 view 에 넘겨줘
            return "Member/createMemberForm";
        }

        // form 정보 Member 객체 생성해서 service 에 전달
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPwd());
        member.setUserName(form.getName());
        member.setMemberType(MemberType.USER);

        //
        memberService.join(member);

        return "redirect:/";
    }



/*    @GetMapping("/")
    public String home(){
        return "Board/mainBoard";
    }*/

}
