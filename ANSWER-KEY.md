# 演练答案卡（讲师用，勿提交给学员前先讲解）

本演示库共埋入 **9 个隐患**，覆盖 SAA 项目真实高风险场景。下表为"坑位—拦截层—修复"对照，供现场演示时逐一揭晓。

| 编号 | 文件 | 隐患 | 级别 | 谁能拦 | 对应 SAA 真实风险 |
|------|------|------|------|--------|-------------------|
| ① | AuthController.java | 日志打印整个 token 对象 | HIGH | AI 审查 | UAP Token 泄露到日志 |
| ② | AuthController.java | Controller 返回含 accessToken | CRITICAL | AI 审查 | 接口下发敏感字段 |
| ③ | OrderController.java | orderId 缺参数校验 | MEDIUM | AI 审查 / Checkstyle | Controller 参数校验规范 |
| ④ | OrderService.java | @Transactional 缺 rollbackFor + 吞异常 | HIGH | AI 审查 | Service 事务失效 |
| ⑤ | UserMapper.xml | ${} 拼接 SQL 注入 | CRITICAL | AI 审查 | MyBatis 注入风险 |
| ⑥ | UserMapper.xml | 查询漏 tenant_id 隔离 | HIGH | AI 审查 | 多租户越权 |
| ⑦ | application.yml | 数据库密码硬编码 | CRITICAL | GitLeaks | 配置密钥硬编码 |
| ⑧ | application.yml | JWT 密钥硬编码 | CRITICAL | GitLeaks | 配置密钥硬编码 |
| ⑨ | application.yml | 云 AccessKey 硬编码 | CRITICAL | GitLeaks | 配置密钥硬编码 |

## 三道防线分工

- **GitLeaks（第一道）**：正则匹配，秒级拦截硬编码密钥 → ⑦⑧⑨
- **Checkstyle（第二道）**：规范类问题 → ③ 部分
- **AI 审查（第三道）**：需理解语义的业务安全 → ①②③④⑤⑥

## 演示节奏建议（每坑约 2 分钟）

1. 展示坑代码 → 提问"这里有什么问题？"（留悬念）
2. 提交 → 触发拦截 → 展示拦截输出
3. 揭晓修复方案（对照 UserMapper.xml 中的 findByIdSafe 正例）
4. 强调对应的 SAA 真实场景

## 修复参考

- ①② → 用 TokenVO，日志只打 userId
- ③ → `@Validated` + `@NotBlank`
- ④ → `@Transactional(rollbackFor = Exception.class)`，移除吞异常的 catch
- ⑤ → `${username}` 改 `#{username}`
- ⑥ → 补 `AND tenant_id = #{tenantId}`
- ⑦⑧⑨ → 改用环境变量 / 配置中心，`${DB_PASSWORD}`
