package com.demo.mapper;

import com.demo.domain.User;

import java.util.List;

/**
 * 用户数据访问（演示桩，SQL 见 resources/UserMapper.xml）
 */
public interface UserMapper {
    User findByUsername(String username);

    List<User> listAllUsers();

    User findByIdSafe(String id, String tenantId);
}
