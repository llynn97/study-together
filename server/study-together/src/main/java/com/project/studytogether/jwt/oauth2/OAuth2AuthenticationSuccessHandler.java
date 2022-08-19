package com.project.studytogether.jwt.oauth2;

import com.google.gson.Gson;
import com.project.studytogether.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = tokenProvider.createAccessToken(authentication);
        tokenProvider.createRefreshToken(authentication, response);
        setSuccessResponse(response, "success to login");

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
