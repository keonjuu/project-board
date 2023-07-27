package exc.Board.controller.Board;

import exc.Board.controller.SessionConst;
import exc.Board.domain.Board;
import exc.Board.domain.member.Member;
import exc.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("boards/new")
    public String createBoardForm(Model model){
        model.addAttribute("boards", new BoardForm());
        return "Board/registerBoard";
    }

    @PostMapping("boards/new")
    public String register(@Valid @ModelAttribute("boards") BoardForm form, BindingResult bindingResult, HttpServletRequest request){

        if(form.getContent().length()<10) {
            bindingResult.rejectValue("content", "content","최소 10자 이상 입력해야 합니다. 현재 글자수="+ form.getContent().length() );
        }

        if (bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            log.error("form.getContent().length()= {}", form.getContent().length());

            return "Board/registerBoard";
        }

        log.info("form = {}" , form);
         // form 정보 board db 에 저장
        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setBoardCategory(form.getCategory());

        // regId, ModId 는 세션정보로 사용
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute("loginMember");

        board.setRegId(loginMember.getEmail());
        board.setRegTime(LocalDateTime.now());
        board.setModTime(LocalDateTime.now());
        board.setModId(loginMember.getEmail());

//        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요

        System.err.println("저장시 board = " + board.toString());
        boardService.save(board);
        return "redirect:/";
    }

    // 상세페이지 조회
    @GetMapping("/Board/{boardNo}/view")
    public String boardView(@PathVariable("boardNo") Long boardNo, Model model){

        // 게시글번호로 db조회
        Board board = boardService.findOne(boardNo);
        // model 에 담아서 전달
        model.addAttribute("board", board);
        return "Board/view";
    }

    // 수정
    @ResponseBody
    @PostMapping("/Board/{boardNo}/view")
    public String boardEditView(@ModelAttribute("boards") BoardForm form, Model model) {

        // 게시글번호로 db조회 -> 영속상태 만들기
        Board board = boardService.findOne(form.getBoardNo());
        System.out.println("board.toString() = " + board.toString());

        // 수정된 값을 화면에서 받아오기
//        board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setBoardNo(form.getBoardNo());
        board.setModTime(LocalDateTime.now());

        System.out.println("board.toString() = " + board.toString());

        // 객체로 받은 값을 db에 새로 저장
        boardService.save(board);

        return "<script>alert('수정 사항이 저장되었습니다.');location.href='/';</script>";
    }


    //3. 삭제
    @ResponseBody
    @PostMapping("/Board/{boardNo}/delete")
    public String deleteBoard(@PathVariable("boardNo") Long boardNo, Model model){

        // 게시글번호로 db조회 -> 영속상태 만들기
        Board board = boardService.findOne(boardNo);
        System.out.println("board.toString() = " + board.toString());

        board.setDelYn("Y");
        boardService.save(board);
        log.error("board.getDelYn() ={}" , board.getDelYn());
        return "<script>alert('삭제되었습니다.');location.href='/';</script>";

    }

}
