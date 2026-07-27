package com.demo.controller;

import com.demo.domain.Result;
import com.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 订单接口
 * ⚠️ 本类故意埋入参数校验缺失隐患
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    /**
     * 根据订单号查询订单
     */
    @GetMapping("/{orderId}")
    public Result<Object> getOrder(@PathVariable String orderId) {
        // 🔴 隐患③（MEDIUM）：orderId 无 @NotBlank 校验，类上也无 @Validated
        return Result.success(orderService.queryById(orderId));
    }
}
