package com.Board.Board;

import com.Board.Board.dto.BoardForm;
import com.Board.Board.dto.SearchForm;
import com.Board.Board.entity.BoardCategory;
import com.Board.Login.LoginForm;
import com.Board.Member.entity.Member;
import com.Board.Session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
            return "Member/loginForm";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요

        //세션에 회원데이터가 없으면 home 으로 반환
        if(loginMember == null){
            return "Member/loginForm";
        }
        model.addAttribute("member", loginMember);

        // 게시판 데이터 가져오기
        Page<BoardForm> boards = boardService.findAll(pageable);
        model.addAttribute("boards", boards);

        return "Home/loginHome";

    }
    @GetMapping("/{category}")
//    @ResponseBody
    public String categoryHome(@PathVariable("category") BoardCategory category, Model model, Pageable pageable){

        // 게시판
        Page<BoardForm> boards = boardService.findCategory(category,pageable);
        log.info("boards.getContent = {}", boards.getContent());

        model.addAttribute("boards", boards);
        model.addAttribute("boardCat", category);

        return "Home/loginHome";
    }

//    @PageableDefault(page = 0, size = 10, sort = "boardNo", direction = Sort.Direction.DESC) Pageable pageable
    @GetMapping("/Board/search")
        public String searchKeyword(@RequestParam("searchType") String[] searchTypes
                                ,  @RequestParam("searchKeyword") String[] searchKeywords
                                , Model model, Pageable pageable){

        SearchForm searchForm = createSearchForm(searchTypes, searchKeywords);

        Page<BoardForm> boards = boardService.searchBoardQuerydsl(searchForm, pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("search", searchForm);

        return "/Home/loginHome";
    }

    //검색 조건으로 SearchForm 객체 생성
    private SearchForm createSearchForm(String[] searchTypes, String[] searchKeywords) {
        SearchForm searchForm = new SearchForm();
        for (int i = 0; i < searchTypes.length; i++) {
            String type = searchTypes[i];
            String keyword = searchKeywords[i];

            switch (type) {
                case "regId":
                    searchForm.setRegId(keyword);
                    break;
                case "title":
                    searchForm.setTitle(keyword);
                    break;
                case "content":
                    searchForm.setContent(keyword);
                    break;
            }
        }
        return searchForm;
    }

}
