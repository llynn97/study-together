package com.project.studytogether.controller.api;


import com.project.studytogether.dto.request.SignUpRequestDto;
import com.project.studytogether.dto.response.ResponseDto;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @ResponseBody
    @GetMapping("/login/oauth2/code/kakao")
    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인")
    public void kakaoCallBack(@RequestParam String code ){
        String access_Token=authService.getKaKaoAccessToken(code);
    }

    @ResponseBody
    @PostMapping("/local/new")
    @ApiOperation(value = "일반 회원가입", notes = "일반 회원가입")
    public ResponseDto<?> join(@RequestBody @Validated SignUpRequestDto signUpRequestDto, BindingResult bindingResult){

        return authService.join(signUpRequestDto, UserMethod.일반);
    }

    @ResponseBody
    @GetMapping("/local/checkid/{id}")
    @ApiOperation(value = "아이디 중복 확인", notes = "아이디 중복 확인")
    public ResponseDto<?> duplicateCheckId(@PathVariable String id){
        return authService.duplicateCheckId(id);
    }

    @ResponseBody
    @GetMapping("/local/checkemail/{email}")
    @ApiOperation(value = "이메일 중복 확인", notes = "이메일 중복 확인")
    public ResponseDto<?> duplicateCheckEmail(@PathVariable String email){

       return authService.duplicateCheckEmail(email);
    }

}
