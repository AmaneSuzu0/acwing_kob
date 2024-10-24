<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
    import { MapObject } from "@/assets/scripts/MapObject"
    import { ref, onMounted } from "vue"
    import { useStore } from 'vuex'

    export default {
        setup() {
            const store = useStore();
            let parent = ref(null);
            let canvas = ref(null);

            onMounted(function () {
                store.commit(
                    "updateGameObject",
                    new MapObject(canvas.value.getContext('2d'), parent.value, store)
                );
            });

            return {
                parent,
                canvas
            }
        }

    }
</script>

<style scoped>
    div.gamemap {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>