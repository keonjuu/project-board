package com.Board.Board;

import com.Board.Board.dto.BoardForm;
import com.Board.Board.dto.BoardUpdateForm;
import com.Board.Board.dto.CommentForm;
import com.Board.Board.entity.AttachFile;
import com.Board.Board.entity.Board;
import com.Board.Board.entity.Comment;
import com.Board.Member.MemberService;
import com.Board.Member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final AttachFileService attachFileService;
    private final CommentService commentService;
    private final FileStore fileStore;

    @GetMapping("boards/new")
    public String createBoardForm(Model model){
        model.addAttribute("boards", new BoardForm());
        return "Board/registerBoard";
    }

    @PostMapping("boards/new")
    public String register(@Valid @ModelAttribute("boards") BoardForm form, BindingResult bindingResult, HttpServletRequest request) throws IOException {

        if(form.getContent().length()<10) {
            bindingResult.rejectValue("content", "content","최소 10자 이상 입력해야 합니다. 현재 글자수="+ form.getContent().length() );
        }

        if (bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            log.error("form.getContent().length()= {}", form.getContent().length());

            return "Board/registerBoard";
        }

        log.info("form = {}" , form);

        // regId, ModId 는 세션정보로 사용
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute("loginMember");

        Member findId = memberService.findById(loginMember.getId()); //

        // form 정보 board db 에 저장
        // dto -> Board entity

        // 첨부파일 리스트 생성
        List<AttachFile> storeFiles = fileStore.storeFiles(form.getAttachFiles());

        // Board
        Board board = Board.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .boardCategory(form.getCategory())
                .modId(findId.getEmail())
                .member(findId) // Member 연관관계
                .attachFiles(storeFiles) // AttachFile 연관관계
                .build();


/*        // 연관관계
        board.setMember(findId);*/
//        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // 타입캐스팅필요

/*        // 모든 AttachFile 엔티티에 Board 엔티티 추가 -> 서비스 단에서
        //attachFile.setBoard(board);
        attachFileService.save(storeFiles);*/

        boardService.save(board);

        return "redirect:/";
    }


    // 상세페이지 조회
    @GetMapping("/Board/{boardNo}/view")
    public String boardView(@PathVariable("boardNo") Long boardNo, Model model){

        // 게시글번호로 db 조회
        Board board = boardService.findOne(boardNo);
        BoardForm boardForm=BoardForm.toDTO(board);
        //log.info("BoardForm = {}", boardForm);

        // 댓글목록 조회, 계층 구조로 변경
        List<Comment> result = commentService.createHierarchy(commentService.findAll(boardNo));

        //  dto 변환
        List<CommentForm> commentForms = result
                .stream()
                .map(comment -> CommentForm.toDTO(comment))
                .collect(Collectors.toList());

        // model 에 담아서 전달
        model.addAttribute("board", boardForm);
        model.addAttribute("comment", commentForms);
        return "Board/view";
    }

    // 수정
    @PostMapping("/Board/{boardNo}/view")
    public ResponseEntity<?> boardEditView(@PathVariable("boardNo") Long boardNo
            , @ModelAttribute("boards") BoardUpdateForm form, Model model) throws IOException {

        // 게시글번호로 db 조회 -> 영속상태 만들기
        Board board = boardService.findOne(form.getBoardNo());

        // 기존파일 수정되면
        if(form.getDeletionQueue() != null){
            List<Long> deletionQueue = form.getDeletionQueue();

            for (Long id : deletionQueue) {
                // 디렉토리에 실제 파일 삭제
                fileStore.deleteFile(id);
                // DB 에서 파일 정보 삭제
                board.removeAttachFile(attachFileService.findAttachFileById(id).get()); //attachFile 연관관계 제거
                attachFileService.deleteAttachFileById(id);
            }
            // {title, content} 필드 값 수정
            board = board.toBuilder()
                    .title(form.getTitle())
                    .content(form.getContent())
                    .build();
            log.info("remove file board = {}", board);
        }

        // 첨부파일이 새로 들어오면
        else if(form.getAttachFiles() != null) {

            // 기존 저장된 첨부파일 리스트 확인해서 디렉토리에서 삭제
            List<AttachFile> allFiles = attachFileService.findAttachFilesByBoardNo(boardNo);
            if(!allFiles.isEmpty()){
                for (AttachFile file : allFiles) {
                    fileStore.deleteFile(file.getId());
                }
            }
            // 첨부파일 리스트 새로 생성
            List<AttachFile> storeFiles = fileStore.storeFiles(form.getAttachFiles());

            // {title, content, attachFiles} 필드 값 수정
            board = board.toBuilder()
                    .title(form.getTitle())
                    .content(form.getContent())
                    .attachFiles(storeFiles)
                    .build();
        } else{
            // {title, content} 필드 값 수정
            board = board.toBuilder()
                    .title(form.getTitle())
                    .content(form.getContent())
                    .build();
        }
        // 객체로 받은 값을 db에 새로 저장
        boardService.save(board);

        return ResponseEntity.ok()
                .body(form.toString());
    }


    //3. 삭제
    @ResponseBody
    @PostMapping("/Board/{boardNo}/delete")
    public String deleteBoard(@PathVariable("boardNo") Long boardNo){

        // 해당 게시판 번호 첨부파일 디렉토리에서 삭제
        List<AttachFile> allFiles = attachFileService.findAttachFilesByBoardNo(boardNo);
        if(!allFiles.isEmpty()){
            for (AttachFile file : allFiles) {
                fileStore.deleteFile(file.getId());
            }
        }

        boardService.deleteById(boardNo);
        return "<script>alert('삭제되었습니다.');location.href='/';</script>";

    }
}
