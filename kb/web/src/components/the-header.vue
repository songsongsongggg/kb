<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <!--        v-model:selectedKeys="selectedKeys1"-->
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <a class="login-menu" @click="showLoginModal()">
          登录
        </a>
      </a-menu-item>
    </a-menu>
    <a-modal
        title="用户表单"
        v-model:visible="lgoinModalVisible"
        :confirm-loading="lgoinModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登陆名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent,ref} from 'vue';

export default defineComponent({
  name: 'the-header',
  setup() {

    const loginUser = ref({
      loginName: "test",
      password: "test",
    });

    const lgoinModalVisible = ref(false);
    const lgoinModalLoading = ref(false);
    const showLoginModal = () =>{
      lgoinModalVisible.value = true;
    }
    // 登录
    const login = () => {
      console.log("登录开始")
    };

    return {
      lgoinModalVisible,
      lgoinModalLoading,
      showLoginModal,
      loginUser,
      login,

    };

  }
});
</script>

<style>
  .login-menu {
    float: right;
    color: white;
  }
</style>

