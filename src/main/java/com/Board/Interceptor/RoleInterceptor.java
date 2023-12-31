package com.Board.Interceptor;

import com.Board.Member.entity.Member;
import com.Board.Member.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        Member loginMember = (Member) request.getSession().getAttribute("loginMember");

        log.info("ROLE 권한 체크 요청 = {}", loginMember.getRole());

        if(loginMember.getRole().equals(Role.USER)){
            log.info("USER! 관리자 인증 요청 실패");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script>alert('해당 페이지 접근 권한이 없습니다.');location.href='/';</script>");
//            response.sendRedirect("/");
            return false;
//            return false;
        }
        return true;
    }

/*    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(ex != null){
//            throw new RuntimeException("No access! 관리자 권한이 필요합니다.");
            log.error("No access! 관리자 권한이 필요합니다.");
//            return false;
        }
    }*/
}
