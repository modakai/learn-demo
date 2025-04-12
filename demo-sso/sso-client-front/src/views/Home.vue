<template>
  <div class="home">
    <h1>SSO前端示例首页</h1>
    <div class="user-info" v-if="isLoggedIn">
      <p>当前登录用户: {{ username }}</p>
      <button @click="logout">退出登录</button>
    </div>
    <div class="login-section" v-else>
      <p>您尚未登录</p>
      <button @click="goToLogin">登录</button>
    </div>
    <div class="api-actions">
      <h2>API访问测试</h2>
      <button @click="accessPublicApi">访问公开API</button>
      <button @click="accessSecureApi">访问受保护API</button>
    </div>
    <div v-if="apiResponse" class="api-response">
      <h3>API响应结果:</h3>
      <pre style="color: #000">{{ apiResponse }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getToken, removeToken, getUsernameFromToken, redirectToLogin } from '../utils/auth';
import api from '../api';

const router = useRouter();
const apiResponse = ref(null);
const isLoggedIn = computed(() => !!getToken());
const username = computed(() => getUsernameFromToken());

onMounted(() => {
  // 检查URL中是否有token参数（从SSO服务器重定向回来）
  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get('token');
  
  if (token) {
    // 保存token并清除URL中的token参数
    saveTokenAndRedirect(token);
  }
});

function saveTokenAndRedirect(token) {
  // 保存token
  localStorage.setItem('token', token);
  
  // 获取并清除之前保存的重定向地址
  const redirectPath = localStorage.getItem('redirectAfterLogin') || '/';
  localStorage.removeItem('redirectAfterLogin');
  
  // 使用history API清除URL中的token参数
  const url = new URL(window.location.href);
  url.searchParams.delete('token');
  window.history.replaceState({}, '', url);
  
  // 如果不是当前页，则重定向
  if (redirectPath !== router.currentRoute.value.path) {
    router.push(redirectPath);
  }
}

function goToLogin() {
  // 直接使用redirectToLogin函数
  redirectToLogin(router.currentRoute.value.path);
}

function logout() {
  // 从本地存储中移除token
  removeToken();
  // 可以选择调用SSO服务器的登出接口
  apiResponse.value = null;
}

async function accessPublicApi() {
  try {
    const response = await api.get('/api/public/info');
    apiResponse.value = JSON.stringify(response.data, null, 2);
  } catch (error) {
    apiResponse.value = `错误: ${error.message}`;
  }
}

async function accessSecureApi() {
  try {
    const response = await api.get('/api/secure/userinfo');
    apiResponse.value = JSON.stringify(response.data, null, 2);
  } catch (error) {
    apiResponse.value = `错误: ${error.message}`;
    if (error.response && error.response.status === 401) {
      // 直接重定向到SSO登录页面
      redirectToLogin(router.currentRoute.value.path);
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1, h2, h3 {
  color: #333;
}

button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 15px;
  margin: 5px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.api-response {
  margin-top: 20px;
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}

.api-actions {
  margin-top: 30px;
}

.user-info, .login-section {
  background-color: #e9f7ef;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
</style> 