package exc.Board.controller.admin;

import exc.Board.domain.Board;
import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.service.AdminService;
import exc.Board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;
    private final MemberService memberService; //

    @GetMapping("/admin")
    public String mainAdmin(Model model){
        // 관리자만
        List<Member> memberList = memberService.findAll();
//        System.out.println("memberList = " + memberList);
        model.addAttribute("memberList", memberList);
        return "/admin/adminView";
    }

    @ResponseBody
    @PostMapping("/admin/JoinYn")
    public String JoinYN(@ModelAttribute("memberList") Member memberlist, HttpServletRequest request){
        System.out.println("memberlist = " + memberlist);
        Member member = new Member();

        String status = request.getParameter("status");
        if (status == "Y"){   member.setStatus(MemberStatus.ADMISSION);}
        if( status== "N"){    member.setStatus(MemberStatus.REJECT);}

        adminService.JoinYn(member);
        return "/admin/adminView";
    }


}
