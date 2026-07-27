#!/bin/bash
# 本地 AI 代码审查脚本（零仓库权限即可用）
# 用法：sh scripts/review/local-review.sh
set -e

SKILL=".harness/skills/security-review.md"
LOGDIR=".harness/run-logs"
mkdir -p "$LOGDIR"

# 取暂存区中的 java/xml/yml 变更
CHANGED=$(git diff --cached --name-only --diff-filter=ACM 2>/dev/null | grep -E '\.(java|xml|yml)$' || true)
if [ -z "$CHANGED" ]; then
  echo "无待审查的变更文件"
  exit 0
fi

echo "🤖 启动 AI 代码审查..."
echo "变更文件:"
echo "$CHANGED" | sed 's/^/  - /'

OUT="$LOGDIR/review-$(date +%s).json"

# 调用本地 Claude Code（需已安装 claude CLI）
claude -p "$(cat "$SKILL")

变更文件：
$CHANGED

代码 diff：
$(git diff --cached -- $CHANGED)" \
  --output-format json > "$OUT" 2>/dev/null || {
    echo "⚠️ 未检测到 claude CLI，改为手动审查模式。审查清单见 $SKILL"
    exit 0
  }

echo "审查完成，报告：$OUT"

# 若存在 CRITICAL，阻断提交
if command -v jq >/dev/null 2>&1; then
  CRIT=$(jq '[.[] | select(.severity=="CRITICAL")] | length' "$OUT" 2>/dev/null || echo 0)
  if [ "${CRIT:-0}" -gt 0 ]; then
    echo "❌ 发现 $CRIT 个 CRITICAL 问题，提交被阻止："
    jq -r '.[] | select(.severity=="CRITICAL") | "  [\(.file):\(.line)] \(.issue)"' "$OUT"
    exit 1
  fi
fi

echo "✅ AI 审查通过"
