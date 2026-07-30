package com.demo.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CouponMapper {
    // SQL 拼接注入：code 直接进 SQL；且查询未按 tenant_id 隔离
    List<Map<String, Object>> findByCode(@Param("code") String code);
}
