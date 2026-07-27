package com.demo.service;

import com.demo.domain.Order;
import com.demo.mapper.InventoryMapper;
import com.demo.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单业务
 * ⚠️ 本类故意埋入事务失效隐患
 */
@Slf4j
@Service
public class OrderService {

    private OrderMapper orderMapper;
    private InventoryMapper inventoryMapper;

    public Object queryById(String orderId) {
        return orderMapper.selectById(orderId);
    }

    /**
     * 创建订单并扣减库存
     */
    // 🔴 隐患④（HIGH）：@Transactional 缺少 rollbackFor，且内部 catch 吞掉异常导致事务不回滚
    @Transactional
    public void createOrder(Order order) {
        try {
            orderMapper.insert(order);
            inventoryMapper.deduct(order.getProductId());
        } catch (Exception e) {
            log.error("创建订单失败", e);
            // 异常被吞，事务不会回滚，库存与订单可能不一致
        }
    }
}
