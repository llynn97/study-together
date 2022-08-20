package com.project.studytogether.service;


import com.project.studytogether.dto.request.SignUpRequestDto;
import com.project.studytogether.dto.response.ResponseDto;
import com.project.studytogether.dto.response.ResultResponseDto;
import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.entity.enums.UserStatus;
import com.project.studytogether.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import org.springframework.transaction.annotation.Transactional;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static String basicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlx2rvqRVwn6S5uXPkHl856CcYvV2z8bUMyw&usqp=CAU";
    private static UserStatus basicStatus = UserStatus.정상;

    @Transactional(readOnly = true)
    public ResponseDto<?> duplicateCheckId(String id) {
        boolean checkId = false;
        if (userRepository.findById(id).isEmpty()) {
            checkId = true;
        }
        return new ResponseDto<ResultResponseDto>(200, "success to duplicate-check-id", new ResultResponseDto(checkId));

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> duplicateCheckEmail(String email) {
        boolean checkEmail = false;
        if (userRepository.findByEmailAndMethod(email, UserMethod.일반) == null) {
            checkEmail = true;
        }
        return new ResponseDto<ResultResponseDto>(200, "success to duplicate-check-email", new ResultResponseDto(checkEmail));
    }

    @Transactional
    public ResponseDto<?> join(SignUpRequestDto signUpRequestDto, UserMethod method) {
        boolean checkJoin = true;
        String id = signUpRequestDto.getId();
        String email = signUpRequestDto.getEmail();
        String password = signUpRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        User saveEntity = new User();
        saveEntity = User.builder().email(email).id(id).password(encodedPassword).profileUrl(basicUrl).status(basicStatus).method(method).build();
        try {
            userRepository.save(saveEntity);
        } catch (DataAccessException e) { // 데이터베이스 저장 오류

        }

        return new ResponseDto<>(200, "success to join");
    }


}
