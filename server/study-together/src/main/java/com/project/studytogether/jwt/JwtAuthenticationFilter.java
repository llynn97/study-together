package com.project.studytogether.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.project.studytogether.entity.User;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Setter
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final CookieUtil cookieUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            // 아이디 패스워드로 security 가 알아볼 수 있는 토큰 객체로 변경
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            //authenticationManager에게 토큰을 넘기면 UserDetailsService가 받아서 처리
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            return auth;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtTokenProvider.createAccessToken(authResult);
        response.addHeader(JwtProperties.headerString, JwtProperties.tokenPrefix + token);
        jwtTokenProvider.createRefreshToken(authResult, response);
        setSuccessResponse(response, "success to login");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        setFailResponse(response, "fail to login");

    }

    protected void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        String json = new Gson().toJson(jsonObject);
        response.getWriter().print(json);
    }

    protected void setFailResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 401);
        jsonObject.put("message", message);
        String json = new Gson().toJson(jsonObject);
        response.getWriter().print(json);
    }


}
