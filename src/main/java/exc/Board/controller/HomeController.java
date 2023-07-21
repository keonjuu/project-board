package exc.Board.controller;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import exc.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final BoardService boardService;

//    @GetMapping("/")
    public String home(){
        return "Home/home";
    }

    @GetMapping("/")
    public String loginHome(HttpServletRequest request, Model model){

        // 게시판
        List<Board> boardList = boardService.findAll();
        //System.out.println("boardList = " + boardList);
        model.addAttribute("boardlist", boardList);

        //세션 관리자에 저장된 회원정보 조회
        HttpSession session = request.getSession(false);
        if(session == null){
            return "Home/home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요
/*        System.out.println("loginMember = " + loginMember.toString());*/

        //세션에 회원데이터가 없으면 home으로 반환
        if(loginMember == null){
            return "Home/home";
        }
        model.addAttribute("member", loginMember);
        return "Home/loginHome";

    }

    @GetMapping("/{category}")
    public String noticeHome(@PathVariable("category") BoardCategory category, Model model){

        // 게시판
        List<Board> boardList = boardService.findAll()
                .stream()
                .filter(c -> c.getBoardCategory().equals(category))
                .sorted(Comparator.comparing(Board::getBoardNo).reversed())
                .collect(Collectors.toList());

        model.addAttribute("boardlist", boardList);

        return "Home/loginHome";
    }

}
