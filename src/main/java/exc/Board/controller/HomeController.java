package exc.Board.controller;

import exc.Board.controller.Board.SearchForm;
import exc.Board.controller.Login.loginForm;
import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import exc.Board.domain.member.Member;
import exc.Board.repository.BoardRepository;
import exc.Board.repository.MemberRepository;
import exc.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.PrintStream;
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

/*
//    @ResponseBody
//    @GetMapping("/Board/search")
    public String categoryHome2(
                                @PageableDefault(page = 0, size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable
                                ,Model model,
                                @RequestParam("searchKeyword") String keyword,
                                @RequestParam("searchType") String searchType
//                                @RequestParam(value = "category", required = false) BoardCategory category,
                                ){

        BoardCategory category;
        category = (BoardCategory) model.getAttribute("category");

        Page<Board> boardList = boardService.searchBoard(searchType, keyword, pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("boardCat", category);

//        String list = boardList.toList().toString();
//        return list;
        return "Home/loginHome";
    }
*/

}
