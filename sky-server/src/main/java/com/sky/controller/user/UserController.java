package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
public class UserController {
    @PostMapping("/login")
    Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = new UserLoginVO();
        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    Result<Void> logout() {
        return Result.success();
    }
}
