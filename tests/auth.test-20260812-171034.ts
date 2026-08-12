// 单元测试
describe('auth module', () => {
  it('should validate token', () => {
    const testToken = 'mock-test-token-12345';  // 测试用 token
    expect(validateToken(testToken)).toBe(true);
  });
});
