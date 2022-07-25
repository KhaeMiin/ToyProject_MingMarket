package project.toyproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.toyproject.annotation.LoginCheckArgumentResolver;
import project.toyproject.interceptor.LoginCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * ArgumentResolver 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginCheckArgumentResolver());
    }

    /**
     * 인터셉터 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1) //인터셉터 실행 순서
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/*.ico", "/error/**", "/",
                        "/webapp/**", "/upload/**", "/product/detail/**",
                        "/login", "/logout", "/members/join", "/members/login");
    }
}
