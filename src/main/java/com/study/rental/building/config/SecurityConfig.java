package com.study.rental.building.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // hasRole 에 표기된 권한만 antMatchers에 명시된 url에 접근가능.
                // permitAll() -> 권한없이 허용
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("user/myinfo").hasRole("USER")
                .antMatchers("/**").permitAll()
            .and()
                // formLogin() -> form 기반으로 인증을 진행함. 기본적으로 세션을 사용
                // /login 경로로 시큐리티에서 제공하는 login form 사용가능
                // .loginPage() 를 통해 커스텀한 페이지로 접근가능. (form action 경로와 일치해야함)
                // .defaultSuccessUrl() 로그인 성공시 리다이렉트 (컨트롤러에서 url 매핑이 되어있어야함)
                // .usernameParameter("paramName") -> name=username이 default지만 커스텀가능.
                .formLogin()
//                .loginPage("/user/login")
//                .defaultSuccessUrl("/user/login/result")
//                .permitAll()
            .and()
                // /logout 에 접근하면 세션 제거 (default)
                // .logoutRequestMatcher 로그아웃 경로 커스텀
                // .logoutSuccessUrl -> 로그아웃 성공후 리다이렉트
                // .invalidateHttpSession 세션 초기화
                // .deleteCookies("key") -> 로그아웃시 특정 쿠키 제거
                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/user/logout/result")
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
            .and()
                // 예외 발생시 exceptionHandling을 통해 페이지 이동
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // userDetailService 를 통해 인증에 필요한 정보들을 가져옴 (서비스 클래스에서 loadUserByUsername 구현)
//        // passwordEncoder 비밀번호 암호화
//        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
//    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // DB없이 인메모리에 username, password, role 설정
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("{bcrypt}1111")
                        .roles("USER")
                        .build();

        System.out.println("authorities : " + user.getAuthorities());
        System.out.println("user : " + user.getUsername());
        System.out.println("password : " + user.getPassword());

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user);

        return userDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{bcrypt}1111")
                .roles("USER");
    }
}

