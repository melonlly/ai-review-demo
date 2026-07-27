package com.demo.domain;

import lombok.Data;

/**
 * 令牌信息（含敏感字段，不应直接下发前端 / 打印日志）
 */
@Data
public class TokenInfo {
    private String userId;
    private String accessToken;   // 敏感
    private String refreshToken;  // 敏感
    private Long expireAt;
}
