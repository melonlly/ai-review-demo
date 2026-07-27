---
name: token-leak-in-log
type: security
severity: critical
occurrences: 1
last_seen: 2026-07-10
projects: [ai-review-demo]
---

## 问题模式
日志或 Controller 返回值直接暴露包含 token/password 的对象。

## 触发代码
```java
log.info("登录成功: {}", tokenInfo);   // accessToken 泄露到日志
return Result.success(tokenInfo);       // accessToken 下发前端
```

## 检测要点
- log.* 参数是完整对象（非基本类型），且类名含 Token/Credential/User
- Controller 返回类型含敏感字段且无 VO 转换 / @JsonIgnore

## 标准修复
```java
log.info("登录成功: userId={}, expireAt={}",
    tokenInfo.getUserId(), tokenInfo.getExpireAt());
return Result.success(new TokenVO(tokenInfo));  // 仅暴露非敏感字段
```

## 关联记忆
- [[log-desensitization-guide]]
- [[controller-return-sensitive-field]]
