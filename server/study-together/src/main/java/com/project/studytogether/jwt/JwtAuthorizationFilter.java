package com.project.studytogether.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.project.studytogether.entity.User;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private final UserRepository userRepository;
    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    CookieUtil cookieUtil;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider
            , String SECRET_KEY, String COOKIE_REFRESH_TOKEN_KEY) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.SECRET_KEY = SECRET_KEY;
        this.COOKIE_REFRESH_TOKEN_KEY = COOKIE_REFRESH_TOKEN_KEY;


    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(JwtProperties.headerString);
        if (header == null || !header.startsWith(JwtProperties.tokenPrefix)) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(JwtProperties.tokenPrefix, "");

        if (!isTokenExpired(token, SECRET_KEY)) { //Access Token 이 아직 유효하다면
            String userPK = jwtTokenProvider.getUserPk(token, SECRET_KEY);
            if (userPK != null) {
                User userEntity = userRepository.findByUserId(Long.parseLong(userPK));
                CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
                Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            final Cookie jwtRefreshToken = cookieUtil.getCookie(request, "refreshToken");
            if (jwtRefreshToken != null) {  //refreshToken 이 쿠키에 있으면
                String refreshJwt = jwtRefreshToken.getValue();
                if (refreshJwt != null && !isTokenExpired(refreshJwt, COOKIE_REFRESH_TOKEN_KEY)) { // refreshToken 이 아직 유효하면
                    Long userId = Long.valueOf(jwtTokenProvider.getUserPk(refreshJwt, COOKIE_REFRESH_TOKEN_KEY));
                    String refreshTokenFromDB = userRepository.getRefreshTokenById(userId);
                    if (refreshJwt.equals(refreshTokenFromDB)) {  // accessToken 재발급
                        Authentication auth = jwtTokenProvider.getAuthentication(token, SECRET_KEY);
                        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
                        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String reissuedToken = jwtTokenProvider.createAccessToken(auth);
                        response.addHeader("Authorization", JwtProperties.tokenPrefix + reissuedToken);

                    }
                } else { // refreshToken 이 유효하지 않음

                    // 로그인이 필요함
                }
            }
        }

        chain.doFilter(request, response);
    }

    public Boolean isTokenExpired(String token, String key) throws UnsupportedEncodingException {

        Date expiration = JWT.require(Algorithm.HMAC512(key)).build().verify(token).getExpiresAt();
        return expiration.before(new Date());
    }


}
