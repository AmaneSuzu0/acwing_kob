<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{$store.state.user.username}}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{bot.title}}
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{$store.state.pk.opponent_username}}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 6vh;">
                <button @click="click_match_btn" type="button" class="btn btn-dark btn-lg">{{match_btn_info}}</button>
            </div>
        </div>
    </div>
</template>

<script>
    import { ref } from 'vue'
    import { useStore } from 'vuex'
    import $ from 'jquery'

    export default {
        setup() {
            let match_btn_info = ref("开始匹配");
            const store = useStore();
            let select_bot = ref(-1);
            let bots = ref([]);


            const refresh_bots = function () {//从后端获取某个user的bots的信息
                $.ajax({
                    url: "https://app6679.acapp.acwing.com.cn/api/user/bot/getlist/",
                    type: "get",
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        bots.value = resp;
                    },
                })
            }
            refresh_bots();

            const click_match_btn = function () {
                if (match_btn_info.value === "开始匹配") {
                    match_btn_info.value = "取消匹配";

                    store.state.pk.socket.send(JSON.stringify({
                        event: "start-matching",
                        bot_id: select_bot.value,
                    }));
                } else {
                    match_btn_info.value = "开始匹配";

                    store.state.pk.socket.send(JSON.stringify({
                        event: "stop-matching",
                    }));
                }
            }

            return {
                match_btn_info,
                click_match_btn,
                bots,
                select_bot,
            }
        }
    }
</script>

<style scoped>
    div.matchground {
        width: 60vw;
        height: 70vh;
        margin: 50px auto;
        background-color: rgba(50, 50, 50, 0.6);
    }

    div.user-photo {
        text-align: center;
        padding-top: 16vh;
    }

    div.user-photo>img {
        border-radius: 50%;
        width: 20vh;
    }

    div.user-username {
        text-align: center;
        font-size: 24px;
        font-weight: 650;
        color: wheat;
        padding-top: 2vh;
    }

    div.user-select-bot {
        padding-top: 25vh;
    }

    div.user-select-bot>select {
        width: 60%;
        margin: 0 auto;
    }
</style>