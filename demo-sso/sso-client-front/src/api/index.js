import axios from 'axios';
import { getToken, redirectToLogin } from '../utils/auth';
import router from '../router';

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8081', // 客户端A的后端服务器
  timeout: 5000,
  withCredentials: false, // 不需要携带cookie
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = getToken();
    
    // 如果token存在，添加到请求头
    if (token) {
      config.headers['Authorization'] = token;
    }
    
    return config;
  },
  error => {
    // 请求错误处理
    console.error('请求拦截器错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 请求成功，直接返回数据
    return response;
  },
  error => {
    // 请求失败，根据状态码处理
    if (error.response) {
      switch (error.response.status) {
        case 401: // 未授权或token失效
          console.warn('Token无效或已过期，需要重新登录');
          // 获取当前路径
          const currentPath = router.currentRoute.value.path;
          // 直接重定向到登录页面
          redirectToLogin(currentPath);
          break;
          
        case 403: // 禁止访问
          console.error('您没有权限访问此资源');
          break;
          
        case 500: // 服务器错误
          console.error('服务器错误，请稍后再试');
          break;
          
        default:
          console.error('请求失败', error.response.data);
      }
    } else if (error.request) {
      // 请求发送成功，但没有收到响应
      console.error('未收到服务器响应，请检查网络连接');
    } else {
      // 请求配置出错
      console.error('请求配置错误', error.message);
    }
    
    return Promise.reject(error);
  }
);

export default api; 