package exc.Board.controller.admin;

import exc.Board.controller.Member.MemberForm;
import exc.Board.domain.Board;
import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.repository.MemberRepository;
import exc.Board.service.AdminService;
import exc.Board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;
    private final MemberService memberService; //
    private final MemberRepository memberRepository;

    @GetMapping("/admin")
    public String mainAdmin(Model model){
        // 관리자만
        List<Member> memberList = memberService.findAll();
//        System.out.println("memberList = " + memberList);
        model.addAttribute("memberList", memberList);
        return "/admin/adminView";
    }

//    @ResponseBody
    @GetMapping("/admin/save/{id}/{JoinYn}")
    public String JoinYN( @PathVariable("id") Long id
                        , @PathVariable("JoinYn") String JoinYn
                          , Model Model
                     ){

        Member findMember = memberRepository.find(id);

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
