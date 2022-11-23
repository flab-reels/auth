package com.flabreels.auth.config;


import com.flabreels.auth.oauth.CustomOauth2UserService;
import com.flabreels.auth.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService oauth2UserService;
    private final OAuth2SuccessHandler successHandler;
    /* Security 작동시 Authorize 제외 Method */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().antMatchers("/h2-console/**" , "/favicon.ico" ,"/error", "/auth/**"));
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                // 토큰 사용해야하므로 csrf 비활성화
                .csrf().disable()
                // 같은이유로 session 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .and()
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oauth2UserService)
                .and()
                .and().build();
    }


}
