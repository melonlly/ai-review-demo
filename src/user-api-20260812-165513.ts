// 用户查询接口
export async function getUserData(req, res) {
  const userId = req.query.id;

  // ❌ SQL 注入漏洞:直接拼接用户输入
  const query = `SELECT * FROM users WHERE id = '${userId}'`;
  const result = await db.execute(query);

  return res.json(result);
}
