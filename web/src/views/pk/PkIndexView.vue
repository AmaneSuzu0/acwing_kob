<template>
    <div class="row">
        <div class="col-2">
            <div class="user-info-left"
                v-if="$store.state.pk.status==='playing' && parseInt($store.state.user.id)===parseInt($store.state.pk.a_id)">
                <img :src="$store.state.user.photo" alt="">
                <div class="user-info-left-text">{{$store.state.user.username}}</div>
            </div>
            <div class="user-info-left"
                v-if="$store.state.pk.status==='playing' && parseInt($store.state.user.id)===parseInt($store.state.pk.b_id)">
                <img :src="$store.state.pk.opponent_photo" alt="">
                <div class="user-info-left-text">{{$store.state.pk.opponent_username}}</div>
            </div>
        </div>
        <div class="col-8">
            <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
            <MatchGround v-else-if="$store.state.pk.status==='matching'"></MatchGround>
            <ResultBoard v-if="$store.state.pk.winner != 'none'">
            </ResultBoard>
        </div>
        <div class="col-2">
            <div class="user-info-right"
                v-if="$store.state.pk.status==='playing' && parseInt($store.state.user.id)===parseInt($store.state.pk.a_id)">
                <img :src="$store.state.pk.opponent_photo" alt="">
                <div class="user-info-right-text">{{$store.state.pk.opponent_username}}</div>
            </div>
            <div class="user-info-right"
                v-if="$store.state.pk.status==='playing' && parseInt($store.state.user.id)===parseInt($store.state.pk.b_id)">
                <img :src="$store.state.user.photo" alt="">
                <div class="user-info-right-text">{{$store.state.user.username}}</div>
            </div>
        </div>
    </div>
</template>

<script>
    import PlayGround from "../../components/PlayGround.vue"
    import { onMounted, onUnmounted } from 'vue'
    import { useStore } from 'vuex'
    import MatchGround from "../../components/MatchGround.vue"
    import ResultBoard from "../../components/ResultBoard.vue"

    export default {
        components: {
            PlayGround,
            MatchGround,
            ResultBoard,
        },
        setup() {
            const store = useStore();
            let socketUrl = `wss://app6679.acapp.acwing.com.cn/websocket/${store.state.user.token}/`;
            store.commit("updateWinner", "none");
            store.commit("updateIsRecord", false);
            let socket = null;
            onMounted(function () {
                store.commit("updateOpponent", {
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
                })
                socket = new WebSocket(socketUrl);

                socket.onopen = function () {
                    console.log("connected!");
                    store.commit("updateSocket", socket);
                }

                socket.onmessage = function (msg) {
                    const data = JSON.parse(msg.data);
                    if (data.event === "match-success") {//匹配成功
                        store.commit("updateOpponent", {
                            username: data.opponent_username,
                            photo: data.opponent_photo,
                        })
                        setTimeout(function () {
                            store.commit("updateStatus", "playing");
                        }, 2000);
                        store.commit("updateGame", data.game);

                    } else if (data.event === "move") {
                        console.log(data);
                        const game = store.state.pk.gameObject;
                        const [snake0, snake1] = game.snakes;
                        snake0.set_direction(data.a_direction);
                        snake1.set_direction(data.b_direction);

                    } else if (data.event === "result") {
                        console.log(data);
                        const game = store.state.pk.gameObject;
                        const [snake0, snake1] = game.snakes;

                        if (data.winner === "all" || data.winner === "B") {
                            snake0.status = "die";
                        }
                        if (data.winner === "all" || data.winner === "A") {
                            snake1.status = "die";
                        }
                        store.commit("updateWinner", data.winner);
                    }
                }
                socket.onclose = function () {
                    console.log("disconnected!");
                }
            });

            onUnmounted(function () {
                socket.close();
                store.commit("updateStatus", "matching");
            });
        }

    }

</script>

<style scoped>
    .user-info-left {
        margin-top: 80px;
        text-align: right;
        font-size: 30px;
        font-weight: 700;
        color: dodgerblue;
    }

    .user-info-left>img {
        width: 40%;
        border-radius: 50%;
    }

    .user-info-left-text {
        padding-top: 15px;
        margin-right: 28px;
    }

    .user-info-right {
        margin-top: 80px;
        text-align: left;
        font-size: 30px;
        font-weight: 700;
        color: orangered;
    }

    .user-info-right>img {
        width: 40%;
        border-radius: 50%;
    }

    .user-info-right-text {
        padding-top: 15px;
        margin-left: 28px;
    }
</style>