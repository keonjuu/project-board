package exc.Board.controller.Login;

import exc.Board.controller.SessionConst;
import exc.Board.domain.member.Member;
import exc.Board.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("members/login")
    public String loginMemberFrom(Model model) {
//        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("loginForm", new loginForm());
        return "Member/loginForm";
    }
//    @PostMapping("members/login")
    public String login1(@ModelAttribute("loginForm") @Valid loginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(form.getEmail().isEmpty()){
            bindingResult.rejectValue("email","email","이메일을 반드시 입력해야 합니다.");
        }
        if(form.getPwd().isEmpty()){
            bindingResult.rejectValue("pwd","pwd","비밀번호를 반드시 입력해야 합니다.");
        }
        if(bindingResult.hasErrors()){
//            log.info("errors = {}" ,bindingResult.toString());
            // bindingResult 를 다시 model 에 안담아도 돼! 자동으로 view 에 넘겨줘
            return "Member/loginForm";
        }

        // form 에서 전달받은 email와 password 로 로그인 시도
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());
        //
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디(이메일) 또는 비밀번호가 맞지 않습니다.");
            return "Member/loginForm";
        }

        // 로그인에 성공하면 쿠키를 생성해서 HttpServletResponse 에 담는다.
        // 쿠키 이름은 memberId 이고 회원이 id 를 담아둔다.
        // 시간 설정안하면 세션 쿠키 -> 브라우저 종료시 종료
        Cookie idCookie = new Cookie("userEmail", loginMember.getEmail());
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("members/login")
    public String login(@ModelAttribute("loginForm") @Valid loginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request){
        if(form.getEmail().isEmpty()){
            bindingResult.rejectValue("email","email","이메일을 반드시 입력해야 합니다.");
        }
        if(form.getPwd().isEmpty()){
            bindingResult.rejectValue("pwd","pwd","비밀번호를 반드시 입력해야 합니다.");
        }
        if(bindingResult.hasErrors()){
            log.info("errors = {}" ,bindingResult.toString());
            // bindingResult 를 다시 model 에 안담아도 돼! 자동으로 view 에 넘겨줘
            return "Member/loginForm";
        }

        // form 에서 전달받은 email와 password 로 로그인 시도
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());
        //System.out.println("loginMember = " + loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디(이메일) 또는 비밀번호가 맞지 않습니다.");
//            log.info("bindingResult errors = {}", bindingResult);
            return "Member/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 세션 반환, 없으면 신규 세션을 생성  default: request.getSession(true)
        HttpSession session = request.getSession(); //false이면 세션이 없으면 새로운 세션을 생성하지 않는다.
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
//        return "redirect:/";
    }


    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션 정보 받아와
        HttpSession session = request.getSession(false);
        if(session !=null){
            session.invalidate();
        }
//        return "<script>alert('로그아웃되었습니다');location.href='/';</script>";
        return "redirect:/";
    }
//    @PostMapping("/logout")
    public String logout1(HttpServletResponse response){
        Cookie cookie = new Cookie("userEmail", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
