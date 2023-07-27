package exc.Board.controller;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import exc.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public String loginHome(HttpServletRequest request, Model model, Pageable pageable){

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

        // 게시판 데이터 가져오기
        //List<Board> boardList = boardService.findAll();
        Page<Board> boardList = boardService.findAll(pageable);
        //System.out.println("boardList = " + boardList);
        model.addAttribute("boardlist", boardList);
        return "Home/loginHome";

    }
    @GetMapping("/{category}")
    public String categoryHome(@PathVariable("category") BoardCategory category, Model model, Pageable pageable){

        // 게시판
        Page<Board> boardList = boardService.findCategory(category,pageable);
//        System.out.println("boardList = " + boardList.getContent().stream().filter(board -> board.equals(BoardCategory.notice)).toString());

        model.addAttribute("boardlist", boardList);
        model.addAttribute("boardCat", category);

        return "Home/loginHome";
    }

}
