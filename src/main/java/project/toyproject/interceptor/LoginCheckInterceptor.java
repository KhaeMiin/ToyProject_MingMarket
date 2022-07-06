package project.toyproject.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.toyproject.controller.LoginController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("로그인 인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(LoginController.LOGIN_MEMBER) == null) {
            log.info("로그인 상태가 아닌 사용자 요청입니다.");
            //로그인폼으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI); //로그인 후 다시 요청했던 페이지로 넘어올 수 있도록
        }

        return true;
    }
}
