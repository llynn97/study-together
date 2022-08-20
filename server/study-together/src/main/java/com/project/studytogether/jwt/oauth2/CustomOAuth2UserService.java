package com.project.studytogether.jwt.oauth2;

import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.entity.enums.UserStatus;
import com.project.studytogether.jwt.CustomUserDetails;
import com.project.studytogether.jwt.oauth2.KakaoUserInfo;
import com.project.studytogether.jwt.oauth2.OAuth2UserInfo;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = new KakaoUserInfo(attributes);
        if (oAuth2UserInfo.getEmail().isEmpty()) {
            // 예외처리
        }
        User user = userRepository.findByEmailAndMethod(oAuth2UserInfo.getEmail(), UserMethod.카카오);
        if (user == null) {
            user = createUser(oAuth2UserInfo);
        }
        return CustomUserDetails.create(user, oAuth2User.getAttributes());

    }

    private User createUser(OAuth2UserInfo userInfo) {

        Long lastUserId = userRepository.maxUserId();
        if (lastUserId == null) {
            lastUserId = 0L;
        }
        Long nxtUserId = lastUserId + 1;
        String originId = userInfo.getName() + nxtUserId + randomNumGen();
        String password = passwordEncoder.encode(userInfo.getEmail() + "kakao");
        String basicUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlx2rvqRVwn6S5uXPkHl856CcYvV2z8bUMyw&usqp=CAU";
        UserStatus basicStatus = UserStatus.정상;
        User user = User.builder()
                .email(userInfo.getEmail())
                .id(originId)
                .password(password)
                .method(UserMethod.카카오)
                .status(basicStatus)
                .profileUrl(basicUrl)
                .build();
        return userRepository.save(user);
    }

    private String randomNumGen() {

        Random random = new Random();
        String numStr = "";

        for (int i = 0; i < 4; i++) {
            //0~9 난수 생성
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        return numStr;
    }
}
