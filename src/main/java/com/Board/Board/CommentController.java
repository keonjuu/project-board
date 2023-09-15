package com.Board.Board;


import com.Board.Board.dto.CommentRegisterForm;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ToString
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/comment/{boardNo}/new")
    public void commentRegister(@PathVariable("boardNo") Long boardNo, @ModelAttribute("comments") CommentRegisterForm form){
        log.info("form = {}",  form);

        commentService.save(form);
    }

    @PostMapping("/comment/{commentNo}/delete")
    public String commentDelete(@PathVariable Long commentNo){
        try{
            // 해당 comment 삭제
            commentService.deleteByComment(commentNo);

            return "success";
        } catch (Exception e) {
            return "error"; // 삭제 오류 시 "error" 반환
        }
    }

}
