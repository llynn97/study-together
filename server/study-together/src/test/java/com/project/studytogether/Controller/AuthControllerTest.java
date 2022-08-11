package com.project.studytogether.Controller;

import com.project.studytogether.dto.request.SignUpRequestDto;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {



    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AuthService authService;




    @Test
    @Transactional
    @WithMockUser
    @DisplayName("회원가입 정상 ")
    public void join() throws Exception {
        Map<String, String> SignUpRequestDto = new HashMap<>();
        SignUpRequestDto.put("id","aaa");
        SignUpRequestDto.put("email","1234@gmail.com");
        SignUpRequestDto.put("password","1234@abc");

        mvc.perform(post("/api/v1/auth/local/new").contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(SignUpRequestDto)))

                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("success to join"));

    }

    @Test
    @DisplayName("아이디 중복 체크 정상 - 중복된 아이디가 없을 때")
    @Transactional
    public void duplicateCheckId() throws Exception {
        String id = "bbb";
        Map<String, Boolean> data = new HashMap<>();
        data.put("result",true);
        mvc.perform(get("/api/v1/auth/local/checkid/"+id).with(SecurityMockMvcRequestPostProcessors.csrf()))

                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(data));
    }

    @Test
    @DisplayName("아이디 중복 체크  - 중복된 아이디가 있을 때")
    @Transactional
    public void duplicateCheckId2() throws Exception {
        String id="bbb";

        //아이디가 bbb 인 회원이 이미 존재함
        SignUpRequestDto signUpRequestDto=new SignUpRequestDto();
        signUpRequestDto.setId("bbb");
        signUpRequestDto.setEmail("123d@gmail.com");
        signUpRequestDto.setPassword("123a$dfs");
        authService.join(signUpRequestDto,UserMethod.일반);


        Map<String, Boolean> data = new HashMap<>();
        data.put("result",false);
        mvc.perform(get("/api/v1/auth/local/checkid/"+id).with(SecurityMockMvcRequestPostProcessors.csrf()))

                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(data));
    }

    @Test
    @DisplayName("이메일 중복 체크 정상 - 중복된 이메일이 없을 때")
    @Transactional
    public void duplicateCheckEmail() throws Exception {
        String email="1234@gmail.com";
        Map<String, Boolean> data = new HashMap<>();
        data.put("result",true);
        mvc.perform(get("/api/v1/auth/local/checkemail/"+email).with(SecurityMockMvcRequestPostProcessors.csrf()))

                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(data));
    }

    @Test
    @DisplayName("이메일 중복 체크  - 중복된 이메일이 있을 때")
    @Transactional
    public void duplicateCheckEmail2() throws Exception {
        String email="123d@gmail.com";

        //이메일이 123d@gmail.com 인 일반 회원가입 회원이 이미 존재함
        SignUpRequestDto signUpRequestDto=new SignUpRequestDto();
        signUpRequestDto.setId("bbb");
        signUpRequestDto.setEmail("123d@gmail.com");
        signUpRequestDto.setPassword("123a$dfs");
        authService.join(signUpRequestDto,UserMethod.일반);

        Map<String, Boolean> data = new HashMap<>();
        data.put("result",false);
        mvc.perform(get("/api/v1/auth/local/checkemail/"+email).with(SecurityMockMvcRequestPostProcessors.csrf()))

                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(data));
    }



}
