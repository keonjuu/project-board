package exc.Board.controller;

import exc.Board.controller.Board.BoardForm;
import exc.Board.controller.Login.LoginForm;
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

    @GetMapping("/")
    public String loginHome(@ModelAttribute("loginForm") LoginForm form, HttpServletRequest request, Model model, Pageable pageable){

        //세션 관리자에 저장된 회원정보 조회
        HttpSession session = request.getSession(false);
        if(session == null){
//            return "Home/home";
            return "Member/loginForm";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요
/*        log.info("loginMember = {}" , loginMember.toString());*/

        //세션에 회원데이터가 없으면 home 으로 반환
        if(loginMember == null){
//            return "Home/home";
            return "Member/loginForm";
        }
        model.addAttribute("member", loginMember);

        // 게시판 데이터 가져오기
        //List<Board> boardList = boardService.findAll();
        Page<BoardForm> boardList = boardService.findAll(pageable);
        //log.info("boardList = {}" , boardList.getContent());
        model.addAttribute("boardList", boardList);

        return "Home/loginHome";

    }
    @GetMapping("/{category}")
//    @ResponseBody
    public String categoryHome(@PathVariable("category") BoardCategory category, Model model, Pageable pageable){

        // 게시판
        Page<BoardForm> boardList = boardService.findCategory(category,pageable);
        log.info("boardList.getContent = {}", boardList.getContent());
//       log.info("boardList = {}" , boardList.getContent().stream().filter(board -> board.equals(BoardCategory.values())).toString());

        model.addAttribute("boardList", boardList);
        model.addAttribute("boardCat", category);

//        return boardList.getContent().toString();
        return "Home/loginHome";
    }

//    @PageableDefault(page = 0, size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable
    @GetMapping("/Board/search")
    public String searchKeyword(@RequestParam("searchType") String searchType
                                ,  @RequestParam("searchKeyword") String keyword
                                , Model model, Pageable pageable){

        log.info("searchType= {}, keyword= {}", searchType, keyword);

        Page<BoardForm> boardList = boardService.searchBoard(searchType, keyword, pageable);
//        log.info("Board/search boardList = {}" , boardList.getContent());
        model.addAttribute("boardList", boardList);

        return "/Home/loginHome";
    }

}
