package exc.Board.controller.Member;

import exc.Board.domain.Board;
import exc.Board.domain.member.Member;
import exc.Board.domain.member.Role;
import exc.Board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // user 사용자정보 조회
    @GetMapping("/member/{userNo}")
    public String memberInfo(@PathVariable("userNo") Long id, Model model, Pageable pageable){
        //
        Member findMember = memberService.findById(id);
        model.addAttribute("member", findMember);
//        long totalElementsNumber = memberService.findAllWithBoard(id, pageable).getTotalElements();
//        model.addAttribute("totalElementsNumber", totalElementsNumber);
        return "Member/memberView";
    }

    // user별 작성글 목록 조회
    @GetMapping("/member/{userNo}/board")
    public String memberBoardInfo(@PathVariable("userNo") Long id, Model model, Pageable pageable){

        Page<Board> memberBoard = memberService.findAllWithBoard(id, pageable);
        System.out.println("memberBoard = " + memberBoard);

        model.addAttribute("boardlist", memberBoard);
        return "Member/memberBoardList";
    }


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
        if(memberService.validDuplicatedEmail(form.getEmail())){
            log.info("result? = {}" , memberService.validDuplicatedEmail(form.getEmail()));
            log.info("email errors = {}" ,bindingResult.toString());
            bindingResult.rejectValue("email","email","이미 사용중인 이메일입니다.");
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
            member.setRole(Role.USER);
            memberService.join(member);

        return "redirect:/";
    }


}
