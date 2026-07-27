package com.demo.domain;

import lombok.Data;

/**
 * 用户实体
 */
@Data
public class User {
    private String id;
    private String username;
    private String password;   // 敏感
    private String tenantId;
    private Long createTime;
}
