package com.demo.controller;

import com.demo.domain.LoginDTO;
import com.demo.domain.TokenInfo;
import com.demo.domain.Result;
import com.demo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 * ⚠️ 本类故意埋入 2 个安全隐患，用于演示 AI 审查拦截能力
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<TokenInfo> login(@RequestBody LoginDTO dto) {
        TokenInfo token = authService.generateToken(dto);

        // 🔴 隐患①（HIGH）：整个 token 对象打到日志，accessToken 明文泄露
        log.info("用户登录成功: {}", token);

        // 🔴 隐患②（CRITICAL）：直接返回含 accessToken 的完整对象给前端
        return Result.success(token);
    }
}
