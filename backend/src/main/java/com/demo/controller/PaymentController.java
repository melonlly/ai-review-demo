package com.demo.controller;

import com.demo.domain.Result;
import com.demo.domain.TokenInfo;
import com.demo.mapper.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 支付查询接口
 * ⚠️ 本类故意埋入多个安全隐患，用于演示 AI 审查拦截能力
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private PaymentMapper paymentMapper;

    /**
     * 按关键字查询支付记录
     */
    @GetMapping("/search")
    public Result<List<Map<String, Object>>> search(@RequestParam String keyword,
                                                     @RequestHeader("X-Token") TokenInfo token) {
        // 🔴 隐患：整个 token 对象打到日志，accessToken 明文泄露
        log.info("查询支付记录, token={}, keyword={}", token, keyword);

        // 🔴 隐患：keyword 直接拼进 SQL（${}），存在 SQL 注入
        //         且查询未按 tenant_id 隔离，存在多租户越权
        String sql = "SELECT * FROM payment WHERE remark LIKE '%" + keyword + "%'";
        List<Map<String, Object>> rows = paymentMapper.rawQuery(sql);

        // 🔴 隐患：直接把含 accessToken 的完整对象回给前端
        return Result.success(rows);
    }
}
