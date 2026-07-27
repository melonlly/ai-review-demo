package com.demo.domain;

import lombok.Data;

/**
 * 订单实体
 */
@Data
public class Order {
    private String orderId;
    private String productId;
    private Integer quantity;
    private String tenantId;
}
