import { createRouter, createWebHistory } from 'vue-router';
import { isTokenValid, redirectToLogin } from '../utils/auth';

// 导入页面组件
import Home from '../views/Home.vue';
import Callback from '../views/Callback.vue';
import Secure from '../views/Secure.vue';

// 定义路由
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/callback',
    name: 'Callback',
    component: Callback
  },
  {
    path: '/secure',
    name: 'Secure',
    component: Secure,
    meta: { requiresAuth: true }
  }
];

// 创建路由器
const router = createRouter({
  history: createWebHistory(),
  routes
});

// 导航守卫
router.beforeEach((to, from, next) => {
  // 检查页面是否需要认证
  if (to.meta.requiresAuth) {
    // 检查是否已登录
    if (!isTokenValid()) {
      // 直接重定向到SSO登录页面
      redirectToLogin(to.fullPath);
      return;
    }
  }
  
  // 继续导航
  next();
});

export default router; 