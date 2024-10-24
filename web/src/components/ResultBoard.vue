<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.winner==='all'">
            平局
        </div>
        <div class="result-board-text"
            v-else-if="$store.state.pk.winner==='A'&& $store.state.pk.a_id==$store.state.user.id">
            胜利
        </div>
        <div class="result-board-text"
            v-else-if="$store.state.pk.winner==='B'&& $store.state.pk.b_id==$store.state.user.id">
            胜利
        </div>
        <div class="result-board-text" v-else>
            失败
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-dark btn-lg">
                再来一局
            </button>
        </div>
    </div>
</template>

<script>
    import { useStore } from 'vuex'

    export default {
        setup() {
            const store = useStore();

            const restart = () => {
                store.commit("updateStatus", "matching");
                store.commit("updateWinner", "none");
                store.commit("updateOpponent", {
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
                })
            }

            return {
                restart,
            };
        }
    }
</script>

<style scoped>
    div.result-board {
        height: 30vh;
        width: 20vw;
        background-color: rgba(50, 50, 50, 0.6);
        position: absolute;
        top: 30vh;
        left: 40vw;
    }

    div.result-board-text {
        text-align: center;
        color: white;
        font-size: 50px;
        font-weight: 650;
        font-style: italic;
        padding-top: 7vh;
    }

    div.result-board-btn {
        text-align: center;
        padding-top: 3vh;
    }
</style>