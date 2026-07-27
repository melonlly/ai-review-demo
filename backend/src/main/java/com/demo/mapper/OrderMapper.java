package com.demo.mapper;

import com.demo.domain.Order;

/**
 * 订单数据访问（演示桩）
 */
public interface OrderMapper {
    Order selectById(String orderId);

    int insert(Order order);
}
