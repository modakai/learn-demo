<template>
  <div class="callback">
    <h2>正在处理登录...</h2>
    <div class="loader"></div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

onMounted(() => {
  // 从URL获取token参数
  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get('token');
  
  if (token) {
    // 保存token到本地存储
    localStorage.setItem('token', token);
    
    // 获取之前保存的路径（如果有）
    const redirectPath = localStorage.getItem('redirectAfterLogin') || '/';
    localStorage.removeItem('redirectAfterLogin');
    
    // 跳转到之前的页面
    router.push(redirectPath);
  } else {
    // 没有token参数，返回首页
    router.push('/');
  }
});
</script>

<style scoped>
.callback {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}

.loader {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #4CAF50;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-top: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 