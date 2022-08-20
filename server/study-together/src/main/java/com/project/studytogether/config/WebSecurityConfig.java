package com.project.studytogether.config;

import com.project.studytogether.jwt.*;
import com.project.studytogether.jwt.oauth2.CustomOAuth2UserService;
import com.project.studytogether.jwt.oauth2.OAuth2AuthenticationSuccessHandler;
import com.project.studytogether.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PrincipalDetailsService principalDetailsService;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuthenticationSuccessHandler;
    private final CookieUtil cookieUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;


    public WebSecurityConfig(PrincipalDetailsService principalDetailsService, @Lazy CustomOAuth2UserService customOAuth2UserService
            , OAuth2AuthenticationSuccessHandler oAuthenticationSuccessHandler
            , CookieUtil cookieUtil, UserRepository userRepository, JwtTokenProvider jwtTokenProvider
            , @Value("${app.auth.token.secret-key}") String SECRET_KEY
            , @Value("${app.auth.token.refresh-cookie-key}") String COOKIE_REFRESH_TOKEN_KEY) {
        this.principalDetailsService = principalDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.userRepository = userRepository;
        this.cookieUtil = cookieUtil;
        this.oAuthenticationSuccessHandler = oAuthenticationSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.SECRET_KEY = SECRET_KEY;
        this.COOKIE_REFRESH_TOKEN_KEY = COOKIE_REFRESH_TOKEN_KEY;
    }


    @Bean
    DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.principalDetailsService);

        return daoAuthenticationProvider;
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager(), userRepository, cookieUtil, jwtTokenProvider);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/local");
        return jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(jwtAuthenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository, jwtTokenProvider, SECRET_KEY, COOKIE_REFRESH_TOKEN_KEY))
                .authenticationProvider(authenticationProvider())
                .oauth2Login().authorizationEndpoint().baseUri("/oauth2/authorize")
                .and().redirectionEndpoint().baseUri("/api/v1/auth/oauth2/callback/**")
                .and().userInfoEndpoint().userService(customOAuth2UserService)
                .and().successHandler(oAuthenticationSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();

    }


}
