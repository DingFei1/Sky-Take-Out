package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Customer Side User Login
 */
@Data
public class UserLoginDTO implements Serializable {
    private String code;
}
