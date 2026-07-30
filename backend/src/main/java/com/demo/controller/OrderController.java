package com.demo.controller;

import com.demo.domain.Result;
import com.demo.domain.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private JdbcTemplate jdbc;

    @GetMapping("/search")
    public Result<List<Map<String, Object>>> search(@RequestParam String keyword,
                                                     @RequestHeader("X-Token") TokenInfo token) {
        log.info("查询订单, token={}, keyword={}", token, keyword);
        String sql = "SELECT * FROM orders WHERE remark LIKE '%" + keyword + "%'";
        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        return Result.success(rows);
    }
}
