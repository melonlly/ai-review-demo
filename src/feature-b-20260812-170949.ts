// Feature B: 配置解析
export function parseConfig(configStr) {
  // ❌ 使用 eval 解析配置
  const config = eval('(' + configStr + ')');
  return config;
}
