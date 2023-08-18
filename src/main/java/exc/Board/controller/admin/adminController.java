package exc.Board.controller.admin;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.service.AdminService;
import exc.Board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;
    private final MemberService memberService;

    @GetMapping("/admin")
    public String mainAdmin(@PageableDefault Pageable pageable, Model model){
        // 관리자만
        Page<Member> memberList = memberService.findAll(pageable);
        model.addAttribute("memberList", memberList);

        return "/admin/adminView";
    }


//    @ResponseBody
    @GetMapping("/admin/save/{id}/{joinYn}")
    public String JoinYN( @PathVariable("id") Long id
                        , @PathVariable("joinYn") String joinYn
                     ){
//        Member findMember = memberRepository.find(id);
        Member findMember = memberService.findById(id);


        if (joinYn.equals("Y")){
            findMember = findMember.toBuilder().status(MemberStatus.ADMISSION).build(); // toBuilder()는 기존 객체의 값을 그대로 복사한 새로운 빌더를 생성
        }
       if( joinYn.equals("N")){
            findMember = findMember.toBuilder().status(MemberStatus.REJECT).build();}
//            findMember.setStatus(MemberStatus.REJECT);}

        adminService.joinYn(findMember);

        return "redirect:/admin";
    }


}
