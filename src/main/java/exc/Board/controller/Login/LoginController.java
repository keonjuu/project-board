package exc.Board.controller.Login;

import exc.Board.controller.SessionConst;
import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Enumeration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginMemberFrom(@ModelAttribute("loginForm") loginForm form) {

//        model.addAttribute("loginForm", new loginForm());
        return "Member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") loginForm form, BindingResult bindingResult
//                        , @RequestParam(defaultValue = "/") String redirectURL
                        , HttpServletRequest request
                        , Model model){

        if(form.getEmail().isEmpty()){
            bindingResult.rejectValue("email","email","이메일을 반드시 입력해야 합니다.");
        }
        if(form.getPwd().isEmpty()){
            bindingResult.rejectValue("pwd","pwd","비밀번호를 반드시 입력해야 합니다.");
        }


        // form 에서 전달받은 email와 password 로 로그인 시도
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());
//        log.info("login? = {}" + loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디(이메일) 또는 비밀번호가 맞지 않습니다.");
//            log.info("bindingResult errors = {}", bindingResult);
            return "Member/loginForm";
        }

        // 가입 승인 체크
        String message = "";
        if(!loginMember.getStatus().equals(MemberStatus.ADMISSION)){
//            message = "<script>alert('가입이 승인되지 않았습니다.');location.href='/login';</scrpt>";
            return "<script>alert('가입이 승인되지 않았습니다.');location.href='/';</script>";
        }

        // 로그인 성공 처리
        // 세션이 있으면 세션 반환, 없으면 신규 세션을 생성  default: request.getSession(true)
        HttpSession session = request.getSession(); //false이면 세션이 없으면 새로운 세션을 생성하지 않는다.
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        // 세션에 저장한 urlPath 가져오기
        String urlPath = (String) session.getAttribute("requestURL");
        String redirectURL = (urlPath == null)? "/" : urlPath;

//        log.info("urlPath = {}" , urlPath);

        //로그인 접속시간 저장
        loginMember.setLastDatetime(LocalDateTime.now());

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
}
