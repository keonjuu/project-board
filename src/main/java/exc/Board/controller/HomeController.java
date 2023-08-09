package exc.Board.controller;

import exc.Board.controller.Login.loginForm;
import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import exc.Board.domain.member.Member;
import exc.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

//    @GetMapping("/")
    public String home(){
        return "Home/home";
    }

    @GetMapping("/")
    public String loginHome(@ModelAttribute("loginForm") loginForm form, HttpServletRequest request, Model model, Pageable pageable){

        //세션 관리자에 저장된 회원정보 조회
        HttpSession session = request.getSession(false);
        if(session == null){
//            return "Home/home";
            return "Member/loginForm";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요
/*        System.out.println("loginMember = " + loginMember.toString());*/

        //세션에 회원데이터가 없으면 home으로 반환
        if(loginMember == null){
//            return "Home/home";
            return "Member/loginForm";
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

        System.out.println("boardList = " + boardList.getContent());
        System.out.println("boardList.getNumber = " + boardList.getNumber());
        model.addAttribute("boardlist", boardList);
        model.addAttribute("boardCat", category);

        return "Home/loginHome";
    }

//    @PageableDefault(page = 0, size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable
    @GetMapping("/Board/search")
    public String searchKeyword(@RequestParam("searchType") String searchType
                                ,  @RequestParam("searchKeyword") String keyword
                                , Model model, Pageable pageable){

        System.out.println("searchType = " + searchType + ", keyword = " + keyword);

        Page<Board> boardlist = boardService.searchBoard(searchType, keyword, pageable);
//        System.out.println("Board/search boardList = " + boardlist.getContent());
        model.addAttribute("boardlist", boardlist);

        return "/Home/loginHome";
    }

}
