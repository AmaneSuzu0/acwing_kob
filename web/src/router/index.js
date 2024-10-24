import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RankListIndexView from '../views/ranklist/RankListIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import RecordPlayBackView from '../views/record/RecordPlayBackView.vue'
import UserBotsIndexView from '../views/user/bots/UserBotsIndexView.vue'
import UserInformation from '../views/user/userinfo/UserInformation.vue'
import NotFoundView from '../views/error/NotFoundView.vue'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView.vue'
import store from '@/store'


const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    meta: {
      requestAuth: true,
    }
  },

  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/:recordId/",
    name: "record_playback",
    component: RecordPlayBackView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bots/",
    name: "user_bots_index",
    component: UserBotsIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/userinfo/",
    name: "user_userinfo_index",
    component: UserInformation,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/error/",
    name: "error_index",
    component: NotFoundView,
    meta: {
      requestAuth: false,
    }
  },

  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView, meta: {
      requestAuth: false,
    }
  },

  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },

  {
    path: "/:catchAll(.*)",
    redirect: "/error/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(function (to, from, next) {
  let flag = 1;
  const jwt_token = localStorage.getItem("jwt_token");
  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getinfo", {
      success() {
      },
      error() {
        store.commit("updateToken", "");
        router.push({ name: 'user_account_login' });
      }
    })
  }
  else {
    flag = 2;
  }

  if (to.meta.requestAuth && !store.state.user.is_login) {
    if (flag == 1) {
      next();
    }
    else {
      //alert("请先登录！");
      next({ name: 'user_account_login' });
    }
  }
  else {
    next();
  }
})

export default router
