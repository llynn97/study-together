package com.project.studytogether.jwt;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    public static final int expirationTime = 1000*60*60; //1시간
    public static final int refreshExpirationTime =  1000*60*60*24*10 ;
    public static final String tokenPrefix = "Bearer "; //JWT 의 prefix는 Bearer
    public static final String headerString = "Authorization";
    public static final String subjectName="stg";

}
