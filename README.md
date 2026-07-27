# ai-review-demo · AI 代码安全审查演练库

> 配套分享《AI 代码安全自动化审查》的实战演示仓库。
> 内含 **9 个精心设计的隐患**，对齐 SAA（智能助手 AI）项目真实高风险场景，
> 用于演示 GitLeaks + Checkstyle + AI 审查 的"三道防线"拦截能力。

---

## 这个库演示什么

一次完整的 **提交 → 拦截 → 修复 → 记忆沉淀** 闭环：

```
开发者提交含隐患的代码
      │
      ▼
┌─────────────────────────────────┐
│ 第一道：GitLeaks   → 拦密钥硬编码 │
│ 第二道：Checkstyle → 拦规范问题   │
│ 第三道：AI 审查    → 拦业务安全   │
└─────────────────────────────────┘
      │
      ▼
生成报告 → 修复 → 通过 → 问题写入 .harness/memory/
```

## 目录结构

```
ai-review-demo/
├── backend/src/main/
│   ├── java/com/demo/
│   │   ├── controller/    # AuthController(坑①②) OrderController(坑③)
│   │   ├── service/       # OrderService(坑④)
│   │   └── domain/        # 实体类
│   └── resources/
│       ├── UserMapper.xml # 坑⑤⑥（SQL注入 / 多租户）
│       └── application.yml# 坑⑦⑧⑨（密钥硬编码）
├── .harness/
│   ├── skills/security-review.md  # AI 审查提示词
│   ├── memory/                    # 历史问题库
│   └── run-logs/                  # 审查日志
├── .github/workflows/code-review.yml  # CI 三道防线
├── scripts/review/
│   ├── local-review.sh   # 本地 AI 审查（零权限方案）
│   └── pre-commit        # 本地 Git 钩子
├── .gitleaks.toml        # 密钥扫描规则
└── ANSWER-KEY.md         # 讲师答案卡（9 个坑对照表）
```

## 快速开始（5 分钟）

```bash
# 1. 创建你自己的 GitHub 仓库并推送
git init && git add . && git commit -m "init: 演练库骨架"
git remote add origin https://github.com/YOUR_NAME/ai-review-demo.git
git push -u origin main

# 2. 安装本地钩子（零仓库权限即可用）
cp scripts/review/pre-commit .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit

# 3. 安装 GitLeaks（可选，用于本地密钥拦截）
#    https://github.com/gitleaks/gitleaks/releases

# 4. 试提交 application.yml，观察密钥被拦截
git add backend/src/main/resources/application.yml
git commit -m "config: 数据库配置"
#    → GitLeaks 拦截 password/jwt-secret/access-key
```

## 演示脚本（现场 15 分钟）

| 环节 | 时长 | 操作 |
|------|------|------|
| 1. 场景引入 | 2min | 展示坑代码，"如果这进了生产…" |
| 2. GitLeaks 拦密钥 | 3min | 提交 application.yml → 秒级拦截 ⑦⑧⑨ |
| 3. AI 拦业务安全 | 5min | 提交 AuthController/UserMapper → AI 输出 ①②⑤⑥ |
| 4. 修复 + 复提交 | 3min | 按 ANSWER-KEY 修复 → 通过 |
| 5. 记忆沉淀 | 2min | 展示 .harness/memory 自动积累 |

> 详细坑位对照与修复方案见 [ANSWER-KEY.md](ANSWER-KEY.md)

## 三道防线覆盖

| 隐患类型 | GitLeaks | Checkstyle | AI 审查 |
|----------|:--------:|:----------:|:-------:|
| 密钥硬编码 ⑦⑧⑨ | ✅ | | |
| 参数校验 ③ | | ✅ | ✅ |
| Token 泄露 ①② | | | ✅ |
| 事务失效 ④ | | | ✅ |
| SQL 注入 ⑤ | | | ✅ |
| 多租户越权 ⑥ | | | ✅ |

## 迁移到内部 GitLab

演练验证后，以下产物可直接复制到内部仓库：
- `.gitleaks.toml` — 密钥扫描规则
- `.harness/skills/` — AI 审查提示词
- `.harness/memory/` — 问题记忆（让 AI 从第一天就"记得"踩过的坑）
- `scripts/review/` — 本地零权限审查方案

零权限场景下，仅用本地 `pre-commit` 钩子 + `local-review.sh` 即可运行，无需 CI/Runner 权限。

---

*⚠️ 本库代码仅用于安全审查演示，隐患均为故意设计，请勿用于生产。*
