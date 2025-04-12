/**
 * 认证相关工具函数
 */

// Token存储的键名
const TOKEN_KEY = 'token';

/**
 * 保存token到localStorage
 * @param {string} token 
 */
export function setToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  }
}

/**
 * 从localStorage获取token
 * @returns {string|null}
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

/**
 * 从localStorage中移除token
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/**
 * 从token中解析用户名
 * 注意：这里使用了一个简单方法，实际项目中可能需要解析JWT或调用API
 * @returns {string|null}
 */
export function getUsernameFromToken() {
  // 这里简单实现，实际中可能需要解析JWT或调用接口
  // 先检查是否已登录
  const token = getToken();
  if (!token) return null;
  
  // 在实际项目中，可以：
  // 1. 解析JWT token (如果token是JWT格式)
  // 2. 调用用户信息接口获取用户信息
  // 3. 从sessionStorage中获取之前存储的用户信息
  
  // 这里返回一个占位符，实际使用中应替换为真实实现
  return "当前登录用户"; // 实际中应改为正确的获取方式
}

/**
 * 检查token是否有效
 * @returns {boolean}
 */
export function isTokenValid() {
  return !!getToken();
}

/**
 * 重定向到登录页面
 * @param {string} currentPath 当前路径，登录后回跳用
 */
export function redirectToLogin(currentPath) {
  if (currentPath) {
    localStorage.setItem('redirectAfterLogin', currentPath);
  }
  
  // 构建重定向URL（当前站点的callback路由）
  const redirectUrl = `${window.location.origin}/callback`;
  // 重定向到SSO服务器登录页
  window.location.href = `http://localhost:8080/login?redirectUrl=${encodeURIComponent(redirectUrl)}`;
} 