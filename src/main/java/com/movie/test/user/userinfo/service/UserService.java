package com.movie.test.user.userinfo.service;

import com.movie.test.user.token.dto.JwtTokenDTO;
import com.movie.test.user.userinfo.dto.*;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserSignupDto userSignupDto);

    UserDto getUserInfo(String userid);
    UserDto getUserInfoByUidAndType(String email, String type);

    JwtTokenDTO loginUser(UserDto userDTO);

    List<String> getUserRoles(String userId);

    UserDto updateUserinfo(UserInfoModifyDto userInfoModifyDto, String loginId);

    Boolean isExistUser(String email);
    Boolean isExistUser(String email, String type);

    void deleteUser(UserDeleteDto userDeleteDto, CustomUser user);
}
