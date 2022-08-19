package com.project.studytogether.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.studytogether.entity.User;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${app.auth.token.secret-key}")
    private String SECRET_KEY;

    @Value("${app.auth.token.refresh-cookie-key}")
    private String COOKIE_REFRESH_TOKEN_KEY;


    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;


    public String createAccessToken(Authentication authentication) throws UnsupportedEncodingException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = String.valueOf(customUserDetails.getUser().getUserId());
        String token = JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.expirationTime))
                .withClaim("userPK", String.valueOf(customUserDetails.getUser().getUserId()))
                .withClaim("email", customUserDetails.getUser().getEmail())
                .withClaim("id", customUserDetails.getUser().getId())
                .sign(Algorithm.HMAC512(SECRET_KEY));

        return token;
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response) throws UnsupportedEncodingException {

        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.expirationTime))
                .sign(Algorithm.HMAC512(COOKIE_REFRESH_TOKEN_KEY));

        saveRefreshToken(authentication, refreshToken);
        Cookie Token = cookieUtil.createCookie("refreshToken", refreshToken, 1000 * 60 * 24 * 2);

        response.addCookie(Token);
    }

    public void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = Long.valueOf(customUserDetails.getUser().getUserId());
        userRepository.updateRefreshToken(id, refreshToken);
    }

    public String getUserPk(String token, String key) throws UnsupportedEncodingException {

        return JWT.require(Algorithm.HMAC512(key)).build().verify(token).getClaim("userPK").asString();
    }

    public Authentication getAuthentication(String accessToken, String key) throws UnsupportedEncodingException {

        Long userPk = Long.valueOf(getUserPk(accessToken, key));
        User user = userRepository.findByUserId(userPk);
        CustomUserDetails principal = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }


}
