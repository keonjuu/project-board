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

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;
    private final MemberService memberService;

/*    @GetMapping("/admin")
    public String mainAdmin(Model model){
        // 관리자만
        List<Member> memberList = memberService.findAll();
//        System.out.println("memberList = " + memberList);
        model.addAttribute("memberList", memberList);
        return "/admin/adminView";
    }
*/

    @GetMapping("/admin")
    public String mainAdmin(@PageableDefault Pageable pageable, Model model){
        // 관리자만
        Page<Member> memberList = memberService.findAll(pageable);
        System.out.println("memberList = " + memberList);
        model.addAttribute("memberList", memberList);
        model.addAttribute("pageNo", memberList.getNumber());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", memberList.hasNext());
        model.addAttribute("hasPrev", memberList.hasPrevious());
        return "/admin/adminView";
    }


//    @ResponseBody
    @GetMapping("/admin/save/{id}/{JoinYn}")
    public String JoinYN( @PathVariable("id") Long id
                        , @PathVariable("JoinYn") String JoinYn
                          , Model Model
                     ){
//        Member findMember = memberRepository.find(id);
        Member findMember = memberService.findById(id);

        if (JoinYn.equals("Y")){
            findMember.setStatus(MemberStatus.ADMISSION);}
        if( JoinYn.equals("N")){
            findMember.setStatus(MemberStatus.REJECT);}

        adminService.JoinYn(findMember);


/*        redirectAttributes.addAttribute("saveid", findMember.getId());
        redirectAttributes.addAttribute("status", findMember.getStatus());*/

        return "redirect:/admin";
    }


}
