package project.toyproject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.toyproject.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인터셉터 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1) //인터셉터 실행 순서
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/*.ico", "/error", "/",
                        "/login", "/logout", "/members/join");
    }
}
