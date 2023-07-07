package exc.Board.controller;

import exc.Board.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "세션이 없습니다.";
        }
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attribute = (String) attributeNames.nextElement();
            System.err.println(attribute+" : "+request.getSession().getAttribute(attribute));
        }

        // 세션데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={} , value={}",name, session.getAttribute(name)));

        System.out.println("session.getAttribute(SessionConst.LOGIN_MEMBER) = " + session.getAttribute(SessionConst.LOGIN_MEMBER));
        System.out.println("session.getAttribute(\"loginMember\") = " + session.getAttribute("loginMember"));
        /*Member loginMember = (Member) session.getAttribute("loginMember");
        System.out.println("loginMember = " + loginMember.getEmail());*/

        log.info("sessionId = {}", session.getId());
        log.info("maxInactiveInterval = {}", session.getMaxInactiveInterval());
        log.info("creationTime = {}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime= {}", session.getLastAccessedTime());
        log.info("isNew= {}", session.isNew());

        return "세션출력";
    }
}
