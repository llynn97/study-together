package com.project.studytogether.jwt;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Component
public class CookieUtil {

    public Cookie createCookie(String cookieName, String value, int maxAge) {
        Cookie token = new Cookie(cookieName, value);
        token.setHttpOnly(true);
        token.setMaxAge(maxAge);
        token.setPath("/");
        return token;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName) {
        final Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

}
