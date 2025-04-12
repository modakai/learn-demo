<template>
  <div class="secure-page">
    <h1>受保护的页面</h1>
    <div class="content">
      <p>这是一个需要登录才能访问的页面。如果您能看到这个页面，说明您已经成功登录。</p>
      <div v-if="userInfo">
        <h2>您的用户信息</h2>
        <pre>{{ userInfo }}</pre>
      </div>
      <div v-else-if="loading">
        <p>正在加载用户信息...</p>
        <div class="loader"></div>
      </div>
      <button @click="goBack">返回首页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../api';
import { getToken, redirectToLogin } from '../utils/auth';

const router = useRouter();
const userInfo = ref(null);
const loading = ref(true);

onMounted(async () => {
  // 获取当前页面的路径
  const currentPath = router.currentRoute.value.path;
  
  if (!getToken()) {
    // 未登录，直接重定向到SSO登录页面
    redirectToLogin(currentPath);
    return;
  }
  
  try {
    // 调用安全API获取用户信息
    const response = await api.get('/api/secure/userinfo');
    userInfo.value = response.data;
  } catch (error) {
    console.error('获取用户信息失败', error);
    if (error.response && error.response.status === 401) {
      // token无效，直接重定向到SSO登录页面
      redirectToLogin(currentPath);
    }
  } finally {
    loading.value = false;
  }
});

function goBack() {
  router.push('/');
}
</script>

<style scoped>
.secure-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.content {
  background-color: #f1f9f1;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px 15px;
  margin-top: 20px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.loader {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #4CAF50;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
  margin: 20px auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 