package exc.Board.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "log_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);
        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);

//        @RequestMapping -HandlerMethod
//        정적 리소스
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
        }
        log.info("REQUEST [{}] [{}] [{}]" , uuid, requestURI, handler);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle {}" , modelAndView);
        //        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        String requestURI = request.getRequestURI();
        String logid = (String) request.getAttribute(LOG_ID);
//        Object logId = request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}] [{}] [{}]" , logid, requestURI, handler);

        if(ex != null){
            log.error("afterCompletion error!!!! ", ex);
        }
    }
}
