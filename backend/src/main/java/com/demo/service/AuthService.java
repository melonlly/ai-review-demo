package com.demo.service;

import com.demo.domain.LoginDTO;
import com.demo.domain.TokenInfo;
import org.springframework.stereotype.Service;

/**
 * 认证业务（演示桩）
 */
@Service
public class AuthService {

    public TokenInfo generateToken(LoginDTO dto) {
        TokenInfo info = new TokenInfo();
        info.setUserId(dto.getUsername());
        info.setAccessToken("mock-access-token");
        info.setRefreshToken("mock-refresh-token");
        info.setExpireAt(System.currentTimeMillis() + 7200_000L);
        return info;
    }
}
