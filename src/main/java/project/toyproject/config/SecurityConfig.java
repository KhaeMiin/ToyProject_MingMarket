package project.toyproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 사용을 위한 Configuration Class를 작성하기 위해서
 * WebSecurityConfigurerAdapter를 상속하여 클래스를 생성하고
 * @Configuration 애노테이션 대신 @EnableWebSecurity 애노테이션을 추가한다.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * PasswordEncoder를 Bean으로 등록
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 인증 or 인가에 대한 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // post 방식으로 값을 전송할 때 token을 사용해야하는 보안 설정을 해제
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/*.ico", "/error", "/",
                        "/webapp/**", "/upload/**", "/product/**",
                        "/login", "/userLogout", "/members/**", "/chat/**").permitAll()
                .anyRequest().authenticated()
        .and() // 로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 시 URL 재정의
                .logoutSuccessUrl("/") // 로그아웃 성공 시 redirect 이동
                .invalidateHttpSession(true) // HTTP Session 초기화
                .deleteCookies("JSESSIONID"); // 특정 쿠키 제거
    }
}