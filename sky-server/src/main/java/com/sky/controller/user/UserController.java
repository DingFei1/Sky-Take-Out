package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
public class UserController {
    @GetMapping("/login")
    Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        //UserLoginVO userLoginVO =
        return null;
    }
}
