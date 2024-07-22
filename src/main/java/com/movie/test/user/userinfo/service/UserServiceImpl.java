package com.movie.test.user.userinfo.service;

import com.movie.test.api.s3.service.S3Service;
import com.movie.test.user.token.dto.JwtTokenDTO;
import com.movie.test.user.token.service.JwtTokenProvider;
import com.movie.test.user.userinfo.dto.UserDto;
import com.movie.test.user.userinfo.dto.UserInfoModifyDto;
import com.movie.test.user.userinfo.dto.UserSignupDto;
import com.movie.test.user.userinfo.entity.UserEntity;
import com.movie.test.user.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDto userSignup(UserSignupDto userSignupDto) {

        if(isExistUser(userSignupDto.getUid(), userSignupDto.getType())){
            UserEntity existUser = userRepository.findByUidAndType(userSignupDto.getUid(), userSignupDto.getType());
            return UserDto.toDTO(existUser);
        }

        UserDto userDto = UserSignupDto.toUserDto(userSignupDto);

        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setNickname(makeNickname());
        if(userDto.getProfileUrl() != null) {
            userDto.setProfileUrl(s3Service.uploadImage(userDto.getProfileUrl()));
        } else {
            // TODO : 기본 프로필사진 설정하기.
        }
        userDto.setRoles("USER;");

        UserEntity user = UserDto.toEntity(userDto);
        UserEntity savedUser = userRepository.save(user);

        return UserDto.toDTO(savedUser);
    }

    public String makeNickname() {

        String[] firstWord = {"영화보는", "친절한", "상냥한", "팝콘먹는", "예매하는", "평화주의"};
        int randomIndex1 = (int)(Math.random() * firstWord.length);

        String[] secondWord = {"케빈", "금자씨", "안톤시거", "트루먼", "코코", "타노스"};
        int randomIndex2 = (int)(Math.random() * secondWord.length);

        String randomNumber = String.valueOf((int)(Math.random() * 10000));

        String nickname = firstWord[randomIndex1] + secondWord[randomIndex2] + randomNumber;

        boolean checkNickname = userRepository.existsByNickname(nickname);
        if (checkNickname) {
            nickname = makeNickname();
        }

        return nickname;
    }

    @Override
    public UserDto getUserInfo(String userid) {

        UserDto userDTO = UserDto.toDTO(userRepository.findById(userid).get());

        return userDTO;
    }

    @Override
    public UserDto getUserInfoByUidAndType(String uid, String type) {

        UserEntity existUser = userRepository.findByUidAndType(uid, type);

        if(existUser != null) {
            return UserDto.toDTO(existUser);
        }

        return null;
    }

    @Override
    public JwtTokenDTO login(UserDto userDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUid(), userDTO.getType());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 토큰 생성
        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.createToken(userDTO, authentication);

        return jwtTokenDTO;
    }

    @Override
    public List<String> checkUserRoles(String userId) {

        return Arrays.stream(userRepository.findById(userId).orElse(new UserEntity()).getRoles().split(",")).toList();
    }

    @Override
    public UserDto updateUserinfo(UserInfoModifyDto userInfoModifyDto, String loginId) {

        UserEntity userEntity = userRepository.findById(loginId).orElseThrow();
        UserEntity modifiedEntity = userEntity.toBuilder()
                .introduction(userInfoModifyDto.getIntroduction())
                .nickname(userInfoModifyDto.getNickname())
                .profileUrl(userInfoModifyDto.getProfileUrl())
                .build();
        UserEntity saved = userRepository.save(modifiedEntity);

        return UserDto.toDTO(saved);
    }

    @Override
    public boolean isExistUser(String uid, String type) {
        boolean isExist = userRepository.existsByUidAndType(uid, type);
        return isExist;
    }
}
