<script setup>
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div class="app">
    <nav class="nav-menu">
      <router-link to="/">首页</router-link>
      <router-link to="/secure">受保护页面</router-link>
    </nav>
    <div class="container">
      <router-view />
    </div>
    <footer class="footer">
      <p>SSO单点登录演示 - 前端示例</p>
    </footer>
  </div>
</template>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
}

.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  color: #000;
}

.nav-menu {
  background-color: #333;
  padding: 1rem;
  text-align: center;
}

.nav-menu a {
  color: white;
  padding: 0.5rem 1rem;
  margin: 0 0.5rem;
  text-decoration: none;
  border-radius: 3px;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  background-color: #4CAF50;
}

.container {
  flex: 1;
  padding: 1rem;
}

.footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}
</style>
