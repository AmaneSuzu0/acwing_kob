<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary" data-bs-theme="dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{name:'home'}">King Of Bots</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
        aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
          <li class="nav-item">
            <router-link :class="route_name == 'pk_index'? 'nav-link active':'nav-link' "
              :to="{name:'pk_index'}">对战</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'record_index'? 'nav-link active':'nav-link' "
              :to="{name:'record_index'}">对局记录</router-link>
          </li>
          <li class="nav-item rank">
            <router-link :class="route_name == 'ranklist_index'? 'nav-link active':'nav-link' "
              :to="{name:'ranklist_index'}">排行榜</router-link>
          </li>

          <form class="d-flex ms-auto me-1" role="search">
            <input class="form-control me-2" type="search" placeholder="搜索" aria-label="Search">
            <button class="btn btn-outline-success custom-hover-btn" type="submit"
              style="border-color: bisque; color: azure; ;">
              <div>搜索</div>
            </button>
          </form>

        </ul>



        <div class="ms-auto">
          <ul class="navbar-nav" v-if="$store.state.user.token">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
                aria-expanded="false">
                {{ $store.state.user.username }}
              </a>
              <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                <li>

                  <router-link class="dropdown-item" :to="{name: 'user_bots_index'}">我的Bot</router-link>
                </li>
                <li>
                  <router-link class="dropdown-item" :to="{name: 'user_userinfo_index'}">个人信息</router-link>
                </li>
                <li>
                  <hr class="dropdown-divider">
                </li>
                <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
              </ul>
            </li>
          </ul>
          <ul class="navbar-nav" v-else>
            <li class="nav-item">
              <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
                登录
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
                注册
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>



<script>
  import { useRoute } from 'vue-router'
  import { computed } from 'vue'
  import { useStore } from 'vuex'

  export default {
    setup() {
      const store = useStore();
      const route = useRoute();
      let route_name = computed(function () {
        return route.name
      })
      const logout = function () {
        store.dispatch("logout");
      }

      return {
        route_name,
        logout,
      }
    }
  }
</script>


<style scoped>
  .btn-outline-success {
    width: 110px;
  }

  .custom-hover-btn:hover {
    background-color: darkgray;
    /* 按钮悬浮时的背景颜色 */
    color: beige;
    /* 按钮悬浮时的文字颜色 */
  }
</style>