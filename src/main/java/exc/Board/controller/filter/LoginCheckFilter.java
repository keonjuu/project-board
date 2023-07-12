package exc.Board.controller.filter;

import exc.Board.controller.SessionConst;
import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.InstanceFilter;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/members/new", "/members/login","/logout","/css/*"};
    private static final String[] adminWhiteList = {"/admin/*"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증체크 필터 시작 {}", requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)== null){
                    log.info("미인증 사용자 요청 {}" , requestURI);
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/members/login?redirectURL=" + requestURI);
                }else{
                    // 관리자 접근
                    if(isAdminLoginCheckPath(requestURI)){
                        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

                        if (MemberType.USER.equals(sessionMember.getMemberType())) {
                            log.info("권한 = > USER");
                            response.setContentType("text/html; charset=utf-8");
                            response.getWriter().println("<script>alert('권한이 없습니다.');location.href='/';</script>");
                        }
                     }
                }

//                LOGIN_MEMBER= Member{id=1, email='keonjuu@innotree.com', userName='박건주', password='kkk', memberGroup=null}
/*
                log.info("LOGIN_MEMBER= {}", session.getAttribute(SessionConst.LOGIN_MEMBER));
                Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//                String string = sessionMember.getMemberType().toString();
                if (MemberType.ADMIN.equals(sessionMember.getMemberType())) {
                    log.info("ADMIN!!!!!!!!!!!");}

                else if (MemberType.USER.equals(sessionMember.getMemberType())) {
                    log.info("USER!!!!!!!!!!!");
                    response.setContentType("text/html; charset=utf-8");
                    response.getWriter().println("<script>alert('권한이 없습니다.');location.href='/';</script>");
                }
*/


            }
           chain.doFilter(request,response);
        } catch (Exception e){ // 예외로깅 가능하지만, 톰캣까지 예외를 보내줘야 함
            throw e;

        }finally {
            log.info("인증체크 필터 종료 {}", requestURI);
        }
    }

    /*
    * 회이트 리스트일 경우 인증 체크 x
    */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }


    private boolean isAdminLoginCheckPath(String requestURI){
        return PatternMatchUtils.simpleMatch(adminWhiteList, requestURI);
    }
}
