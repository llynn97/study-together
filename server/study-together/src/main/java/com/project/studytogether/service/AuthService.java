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
    private static UserStatus basicStatus =UserStatus.정상;

    @Transactional(readOnly=true)
    public ResponseDto<?> duplicateCheckId(String id){
       boolean checkId=false;
       if(userRepository.findById(id).isEmpty()){
          checkId=true;
       }
       return new ResponseDto<ResultResponseDto>(200,"success to duplicate-check-id",new ResultResponseDto(checkId));

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> duplicateCheckEmail(String email){
        boolean checkEmail=false;
        if(userRepository.findByEmailAndMethod(email, UserMethod.일반).isEmpty()){
            checkEmail=true;
        }
        return new ResponseDto<ResultResponseDto>(200,"success to duplicate-check-email",new ResultResponseDto(checkEmail));
    }

    @Transactional
    public ResponseDto<?> join(SignUpRequestDto signUpRequestDto, UserMethod method){
         boolean checkJoin=true;
         String id=signUpRequestDto.getId();
         String email=signUpRequestDto.getEmail();
         String password=signUpRequestDto.getPassword();
         String encodedPassword=passwordEncoder.encode(password);
         User saveEntity=new User();
         saveEntity=User.builder().email(email).id(id).password(encodedPassword).profile_url(basicUrl).status(basicStatus).method(method).build();
         try {
             userRepository.save(saveEntity);
         }catch (DataAccessException e){ // 데이터베이스 저장 오류

         }

         return new ResponseDto<>(200,"success to join");
    }


    public String getKaKaoAccessToken(String code){

        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);  //outputstream 으로 post 데이터를 넘겨주겠다는 옵션

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=b5265b666692b9ecfc198b0784624d97"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8080/api/v1/auth/login/oauth2/code/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode(); //실제 서버로 request 요청
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            br.close();
            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public void getUserInfo(String access_Token){ //카카오 서버에서 사용자 정보를 받아온 후 회원가입이 안되어 있다면 회원가입 후 로그인 response 보냄

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            Long lastUserId = userRepository.maxUserId();
            Long nxtUserId = lastUserId + 1;

            String originId = properties.getAsJsonObject().get("nickname").getAsString();
            String id = originId + nxtUserId.toString() + randomNumGen();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            String password = email + "kakao";
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
            signUpRequestDto.setId(id);
            signUpRequestDto.setEmail(email);
            signUpRequestDto.setPassword(password);
            if(userRepository.findByEmailAndMethod(email,UserMethod.카카오).isEmpty()){ // 회원가입이 안 되어 있다면 회원가입
                join(signUpRequestDto,UserMethod.카카오);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //로그인 한 결과


    }

    public static String randomNumGen() {
        Random random = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            //0~9 난수 생성
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        return numStr;
    }


}
