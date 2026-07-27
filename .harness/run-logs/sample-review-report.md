# AI 代码审查报告（样例 · 演示素材）

> 审查对象：ai-review-demo 演示库 5 个变更文件
> 审查时间：2026-07-27
> 门禁结论：**❌ BLOCK（阻断合并）** —— 存在 5 个 CRITICAL 问题

---

## 汇总

| 级别 | 数量 |
|------|:----:|
| 🔴 CRITICAL | 5 |
| 🟠 HIGH | 3 |
| 🟡 MEDIUM | 1 |
| 合计 | **9** |

拦截来源：GitLeaks 3 项 · AI 审查 6 项（其中 1 项 Checkstyle 亦可覆盖）

---

## 详细清单（按严重程度排序）

### 🔴 CRITICAL

**1. 数据库密码硬编码** · `application.yml:13` · GitLeaks
```yaml
password: MySecretPassword123
```
→ 改用环境变量：`password: ${DB_PASSWORD}`

**2. JWT 密钥硬编码** · `application.yml:17` · GitLeaks
> 泄露后可伪造任意用户令牌。改用 `${JWT_SECRET}`，并轮换现有密钥。

**3. 云 AccessKey 硬编码** · `application.yml:22` · GitLeaks
> 移入 KMS / 环境变量，立即吊销已泄露的 Key。

**4. Controller 下发敏感字段** · `AuthController.java:32` · AI
```java
return Result.success(token);  // accessToken/refreshToken 明文返回前端
```
→ 新建 `TokenVO` 仅暴露 `userId` 与 `expireAt`。

**5. SQL 注入** · `UserMapper.xml:9` · AI
```xml
SELECT * FROM users WHERE username = '${username}'
```
→ 改为 `#{username}` 预编译。

### 🟠 HIGH

**6. 日志泄露 Token** · `AuthController.java:29` · AI
```java
log.info("用户登录成功: {}", token);  // accessToken 进日志文件
```
→ `log.info("用户登录成功: userId={}", token.getUserId())`

**7. 事务失效** · `OrderService.java:29` · AI
```java
@Transactional                     // 缺 rollbackFor
public void createOrder(Order order) {
    try { ... } catch (Exception e) { log.error(...); }  // 吞异常，不回滚
}
```
→ `@Transactional(rollbackFor = Exception.class)`，移除吞异常的 catch。库存扣减失败时订单本应回滚，否则数据不一致。

**8. 多租户越权** · `UserMapper.xml:14` · AI
```xml
SELECT * FROM users ORDER BY create_time DESC  -- 未带 tenant_id
```
→ 增加 `WHERE tenant_id = #{tenantId}`。

### 🟡 MEDIUM

**9. 参数校验缺失** · `OrderController.java:21` · AI / Checkstyle
→ 类加 `@Validated`，`orderId` 加 `@NotBlank`。

---

## 对应 SAA 真实风险

| 本报告发现 | SAA 对应场景 |
|---|---|
| Token 下发/日志泄露（4、6） | UAP Token 泄露 |
| 多租户越权（8） | MyBatis-Plus 多租户拦截器绕过 |
| SQL 注入（5） | MyBatis `${}` 拼接 |
| 事务失效（7） | Service 层 `@Transactional` 误用 |
| 密钥硬编码（1-3） | application.yml 敏感配置 |

> 本报告为演示素材，可在现场无 CLI/网络时直接展示。真实运行请见 `scripts/review/local-review.sh`。
