package com.demo.domain;

import lombok.Data;

/**
 * 登录入参
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}
