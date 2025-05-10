package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User management controller
 */
@RestController
@RequestMapping("/user/user")
public class UserController {
    /**
     * User log in
     * @param userLoginDTO User login data transfer object
     * @return {@code Result<UserLoginVO>} Contains user information (id, token, etc.)
     */
    @PostMapping("/login")
    Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = new UserLoginVO();
        return Result.success(userLoginVO);
    }

    /**
     * User log out
     * @return Operation result with success message
     */
    @PostMapping("/logout")
    Result<Void> logout() {
        return Result.success();
    }
}