package project.toyproject.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.toyproject.controller.LoginController;
import project.toyproject.dto.MemberDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCheckArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginCheckAnnotation = parameter.hasParameterAnnotation(LoginCheck.class); //파라미터에 @LoginCheck 붙어있는지 체크
        boolean hasSessionMemberDataType = MemberDto.SessionMemberData.class.isAssignableFrom(parameter.getParameterType()); //@LoginCheck 붙은 파라미터 타입이 SessionMemberData.class 타입이 들어오는지

        //위 조건이 두개다 true 면 return 실행
        return hasLoginCheckAnnotation && hasSessionMemberDataType;
    }

    /**
     * Controller에 쓰였던 아래 코드를 등록한 것이다.
        HttpSession session = request.getSession(false);//맨 처음 홈화면들어올 때 굳이 세션을 생성할 필요가 없으니 FALSE로
        if (session == null) { //로그인 상태가 아니면
            return "home";
        }

        MemberDto.MemberData loginMember = (MemberDto.MemberData) session.getAttribute("loginMember");
    */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(); //HttpServletRequest 사용할 수 있다.
        HttpSession session = request.getSession(false); //기존 생성된 세션이 없어도 생성 안하도록
        if (session == null) {
            return null; //기존 생성된 세션이 없으면 null을 반환
        }
        return session.getAttribute(LoginController.LOGIN_MEMBER);
    }
}

