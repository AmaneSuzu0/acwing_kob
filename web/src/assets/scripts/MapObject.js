import { AcGameObject } from "./AcGamesObject.js";
import { Snake } from "./Snake.js";
import { Wall } from "./Wall.js";


export class MapObject extends AcGameObject {
    constructor(ctx, parent, store) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.store = store;
        this.inner_walls_cnt = 20;
        this.walls = [];

        this.snakes = [
            new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this),
        ]
    }

    add_listening_events() {//此处选择是对战界面还是回放界面

        if (this.store.state.record.is_record) {
            let k = 0;//第几步了
            const [snake0, snake1] = this.snakes;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const winner = this.store.state.record.record_winner;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if (winner === "all" || winner === "B") {
                        snake0.status = "die";
                    }
                    if (winner === "all" || winner === "A") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);

                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300);
        }
        else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
                else if (e.key === 'ArrowUp') d = 0;
                else if (e.key === 'ArrowRight') d = 1;
                else if (e.key === 'ArrowDown') d = 2;
                else if (e.key === 'ArrowLeft') d = 3;
                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            });
        }
    }


    check_valid(cell) {//检测是否撞墙或者身体
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c) {
                return false;
            }
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {//当蛇尾不会变长的时候不判断尾部是否碰撞
                k--;
            }
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;
            }
        }
        return true;
    }

    creat_walls() {
        //根据标记二维数组g创建障碍物

        const g = this.store.state.pk.gamemap;

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    check_ready() {//检查地图上两条蛇是否都处于就绪状态
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    next_step() {
        for (const snake of this.snakes) {
            snake.next_step();
        }

    }
    start() {
        this.creat_walls();
        this.add_listening_events();
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let i = 0; i < this.cols; i++) {
            for (let j = 0; j < this.rows; j++) {
                if ((i + j) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                }
                else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }
    }

}