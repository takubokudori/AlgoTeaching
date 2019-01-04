package algoteach;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class SortingBlocks {
    private static final int BUBBLE_SORT = 0;
    private static final int SELECTION_SORT = 1;
    private static final int INSERTION_SORT = 2;
    private static final int SHAKER_SORT = 3;
    private static final int SHELL_SORT = 4;
    private static final int COMB_SORT = 5;
    private static final int ODDEVEN_SORT = 6;
    private static final int NANTTYATTE_SELECTION_SORT = 7;
    private static final int HEAP_SORT = 8;
    private static final int HEAP_SORT_FULL = 9;
    private static final int QUICK_SORT = 10;
    private static final int HEAP_CONSTRUCTION = 11;
    private static final int STAIRS = 0;
    private static final int NEEDLE_MOUNTAIN = 1;    // 針山
    private static final int SAWTOOTH_WAVE = 2;        // のこぎり波
    private static final int ALTERNATEII = 3;    // 大小交互
    private static final int SINE_CURVE = 4;    // 正弦
    private static final int COSINE_CURVE = 5;    // 余弦
    private static final int PULSE = 6;
    private static final int DENSE = 7; // 凝集
    private static final int DISCRETE = 8;

    private int gi, gj, gk, gl, minj; // 1つずつやるときのメンバ変数
    private SortAlgorithm algo;
    private Stack<Integer> argStack = new Stack<Integer>();
    private boolean ascending_order = true; // 昇順か降順か
    private boolean gflag;
    /*
	private int NUM=10;
	private int MARGIN=5;
	private int SIZE=10;
	*/
    private int swap_counter = 0;    // 交換変数
    /*
	private int NUM=50;
	private int MARGIN=3;
	private int SIZE=5;
	*/
    private final static int MAX_NUM = 1500;
    private int NUM = 150;
    private int MARGIN = 1;
    private int SIZE = 8;

	/*
	private int NUM=500;
	private int MARGIN=0;
	private int SIZE=1;
	*/

    private int count = 0;
    private int sort_s = 0; // ソートの種類
    boolean state = false;
    Block[] block;
    Block[] workblock;
    int[] a = new int[MAX_NUM];    // 通常配列
    int[] worka = new int[MAX_NUM];
    int[] garg = new int[5]; // グローバル引数(1-origin)
    int[] gap = new int[100];    // シェルソート用ギャップ
    Stack<Integer> gparam = new Stack<Integer>(); // 関数呼び出し引数用

    public SortingBlocks() {
        gap[0] = 1;
        for (int i = 1; gap[i - 1] < MAX_NUM; i++) {    // シェルソート用ギャップ
            gap[i] = 3 * gap[i - 1] + 1;
        }
        block = new Block[MAX_NUM];    // 初期設定した数で生成する
        workblock = new Block[MAX_NUM];
        for (int i = 0; i < MAX_NUM; i++) {
            // 整列ブロックの作成
            block[i] = new Block((SIZE + MARGIN) * (i + 1), MainPanel.HEIGHT - 100 - SIZE * (i + 1), SIZE, SIZE * (i + 1));
            a[i] = i;
        }
        randomSort();
    }

    public SortingBlocks(int N) {
        NUM = N;
        block = new Block[MAX_NUM];    // 初期設定した数で生成する
        workblock = new Block[MAX_NUM];
        for (int i = 0; i < NUM; i++) {
            // 整列ブロックの作成
            block[i] = new Block((SIZE + MARGIN) * (i + 1), MainPanel.HEIGHT - 100 - SIZE * (i + 1), SIZE, SIZE * (i + 1));
            a[i] = i;
        }
        randomSort();

    }

    public int getSwapCounter() {
        return swap_counter;
    }

    public String getText() {
        String str = "";
        for (int i = 0; i < NUM; i++) {
            str = str + String.valueOf(a[i] + 1);
            if (i != NUM - 1) str = str + " ";
        }
        return str;
    }

    public void setA(String[] str) {
        if (!(0 < str.length && str.length <= MAX_NUM)) {
            System.out.println("Error!");
            return;    // 失敗した
        }
        NUM = str.length;
        for (int i = 0; i < str.length; i++) {
            int k = Integer.parseInt(str[i]);
            if (k <= 0) {
                System.out.println("Error!");
                // ダイアログ出したい
                return;
            }
            a[i] = k;
        }
        AutoDispFormat();
        return;
    }

    public int NumChange(int N) {// 要素数変更
        if (!(0 < N && N <= MAX_NUM)) {
            System.out.println("Error!");
            return -1;    // 失敗した
        }

        NUM = N;

        AutoDispFormat();
        for (int i = 0; i < NUM; i++) {
            block[i] = new Block((SIZE + MARGIN) * (i + 1), MainPanel.HEIGHT - 100 - SIZE * (i + 1), SIZE, SIZE * (i + 1));
            a[i] = i;
        }
        return 0;
    }

    private void AutoDispFormat() {
        if (NUM <= 20) {
            MARGIN = 10;
            SIZE = 60;
        } else if (NUM < 55) {
            MARGIN = 5;
            SIZE = 20;
        } else if (NUM <= 80) {
            MARGIN = 4;
            SIZE = 12;
        } else if (NUM <= 120) {
            MARGIN = 2;
            SIZE = 8;
        } else if (NUM <= 170) {
            MARGIN = 1;
            SIZE = 8;
        } else if (NUM <= 200) {
            MARGIN = 1;
            SIZE = 4;
        } else if (NUM <= 300) {
            MARGIN = 1;
            SIZE = 2;
        } else {
            MARGIN = 0;
            SIZE = 2;
        }
    }

    public void randomSort() {
        // ランダムに入れ替える
        for (int i = 0; i < NUM - 1; i++) {
            Random rnd = new Random();
            int r = rnd.nextInt(NUM - i) + i;
            swap(a, i, r);
        }
    }

    public void ijinit(int I, int J) {
        gi = I;
        gj = J;
    }

    public void ijinit(int s, boolean order) { // 各種ソートごとの初期化
        // algo.init();
        sort_s = s;
        ascending_order = order;
        switch (s) {
            case BUBBLE_SORT:
                gi = 0;
                gj = NUM - 1;
                break;
            case SELECTION_SORT:
                gi = 0;
                minj = gi;
                gj = gi + 1;
                break;
            case INSERTION_SORT:
                gi = 1;
                gj = gi - 1;
                break;
            case SHAKER_SORT:
                gi = 0;
                gj = NUM - 1;
                gk = 0;
                gl = NUM - 1;
                count = 0;
                break;
            case SHELL_SORT:
                for (gk = 0; gap[gk] < NUM; gk++) ;    // ギャップ
                gk--;
                gi = gap[gk];
                gj = gi - gap[gk];
                break;
            case COMB_SORT:
                gi = 0;
                gj = 0;
                gk = NUM * 10 / 13;
                gflag = false;
                break;
            case ODDEVEN_SORT:
                gi = 0;
                gj = 0;
                gflag = false;
                break;
            case NANTTYATTE_SELECTION_SORT:
                gi = 0;
                minj = gi;
                gj = gi + 1;
                break;
            case HEAP_SORT:
                if (ascending_order) {
                    for (int i = (NUM - 1) / 2; i >= 0; i--) ascDownHeap(a, i, NUM - 1);
                } else {
                    for (int i = (NUM - 1) / 2; i >= 0; i--) descDownHeap(a, i, NUM - 1);
                }
                gi = NUM - 1;
                break;
            case HEAP_SORT_FULL:
                gj = NUM - 1; // スワップ
                gk = (NUM - 1) / 2; // ヒープ用ループ
                garg[2] = (NUM - 1) / 2;
                garg[3] = NUM - 1;
                gi = garg[2];
                //for(int i=(NUM-1)/2;i>=0;i--)popHeap(a, i, NUM-1);
                break;
            case HEAP_CONSTRUCTION:
                garg[2] = (NUM - 1) / 2;
                garg[3] = NUM - 1; //right
                gi = garg[2]; //parent
                break;
        }
        swap_counter = 0;
    }

    public boolean sort(int s) {

        // algo.step()
        if (ascending_order) {
            switch (sort_s) {
                case BUBBLE_SORT:    // 昇順バブルソート１つ
                    if (a[gj] < a[gj - 1]) {
                        swap(a, gj, gj - 1);
                    }
                    gj--;
                    if (gj <= gi) {
                        gi++;
                        gj = NUM - 1;
                    }
                    if (gi >= NUM - 1) return false;
                    break; // 昇順バブルソート１つ終わり
                case SELECTION_SORT:    // 昇順セレクションソート１つ
                    if (a[gj] < a[minj]) {
                        minj = gj;
                    }
                    gj++;
                    if (gj >= NUM) {
                        swap(a, minj, gi);
                        gi++;
                        minj = gi;
                        gj = gi + 1;
                    }
                    if (gi >= NUM - 1) return false;
                    break; // 昇順セレクションソートおわり
                case INSERTION_SORT:    // 昇順インサーションソート１つ
                    if (a[gj] > a[gj + 1]) {
                        swap(a, gj, gj + 1);
                    } else {
                        gi++;
                        gj = gi + 0;
                    }    // マイナス１は直後にされるから、+0にしておくこと
                    gj--;
                    if (gj <= -1) {
                        gi++;
                        gj = gi - 1;
                    }
                    if (gi >= NUM) {
                        gi = 0;
                        return false;
                    }
                    break; // 昇順インサおわり
                case SHAKER_SORT:    // 昇順シェーカーソート１つ
                    // gkが左、glが右端
                    if (gi % 2 == 0) {
                        if (a[gj] < a[gj - 1]) {    // 交換
                            swap(a, gj, gj - 1);
                            count = gj;
                        }
                        gj--;
                        if (gj <= gk) {    // 反転
                            gi++;
                            gk = count;
                            gj = gk;    // 反転
                        }
                    } else {
                        if (a[gj] > a[gj + 1]) {    // 交換
                            swap(a, gj, gj + 1);
                            count = gj;
                        }
                        gj++;
                        if (gj >= gl) {
                            gi++;
                            gl = count;
                            gj = gl;
                        }
                    }
                    if (gk >= gl) return false;
                    break; // 昇順シェーカーおわり
                case SHELL_SORT:    // 昇順シェルソート１つ
                    if (a[gj] > a[gj + gap[gk]]) {
                        swap(a, gj, gj + gap[gk]);
                    } else {
                        gi += 1/*gap[gk]*/;
                        gj = gi + 0;
                    }    // マイナス１は直後にされるから、+0にしておくこと
                    gj -= gap[gk];
                    if (gj <= -1) {
                        gi += 1/*gap[gk]*/;
                        gj = gi - gap[gk];
                    }
                    if (gi >= NUM) {
                        gk--;
                        if (gk == -1) {
                            gk = 0;
                            return false;
                        }
                        gi = gap[gk];
                        gj = gi - gap[gk];
                    }
                    break; // 昇順シェルおわり
                case COMB_SORT:        // 昇順コムソート１つ
                    if (a[gi] > a[gi + gk]) {
                        swap(a, gi, gi + gk);
                        gflag = false;
                    }
                    gi++;
                    if (gi + gk >= NUM) {
                        if (gk == 1 && gflag) return false;
                        gi = 0;
                        gk = gk * 10 / 13;
                        gflag = true;
                    }
                    if (gk <= 1) {
                        gk = 1;
                    }
                    break;
                case ODDEVEN_SORT: // 昇順奇偶ソート１つ
                    if (gi % 2 == 0) {
                        if (a[gj] > a[gj + 1]) {
                            swap(a, gj, gj + 1);
                            gflag = true;
                        }
                        gj += 2;
                        if (gj >= NUM - 1) {
                            gi++;
                            gj = 1;
                            gflag = false;
                        }
                    } else {
                        if (a[gj] > a[gj + 1]) {
                            swap(a, gj, gj + 1);
                            gflag = true;
                        }
                        gj += 2;
                        if (gj >= NUM - 1) {
                            if (!gflag) return false;
                            gi++;
                            gj = 0;
                            gflag = false;
                        }

                    }
                    break; // 昇順奇偶おわり
                case NANTTYATTE_SELECTION_SORT: // 昇順なんちゃって選択

                    if (a[gj] < a[gi]) {
                        swap(a, gi, gj);
                    }
                    gj++;
                    if (gj >= NUM) {
                        gi++;
                        gj = gi + 1;
                    }
                    if (gi >= NUM - 1) {
                        gi = 0;
                        return false;
                    }
                    break;
                case HEAP_SORT: // 昇順ヒープ(簡略版)
                    if (gi > 0) {
                        swap(a, 0, gi);
                        ascDownHeap(a, 0, gi - 1);
                        gi--;
                    } else return false;
                    break;
                case HEAP_SORT_FULL:
                    if (gflag) {
                        // ヒープ構築
                        System.out.println("constract..." + garg[2] + " " + gi);
                        int ch;
                        // garg[2]=left,garg[3]=right,gi=parent
                        if (gi * 2 + 1 > garg[3]) {
                            // 子がなければ終了．初期化する
                            gflag = false;
                            break;
                        }
                        ch = (gi * 2 + 2 <= garg[3] && a[gi * 2 + 2] > a[gi * 2 + 1]) ? gi * 2 + 2 : gi * 2 + 1;
                        if (a[gi] >= a[ch]) {
                            gflag = false;
                            break;
                        }
                        swap(a, gi, ch);
                        gi = ch;
                    } else { // 順次スワップ
                        if (gk > -1) {
                            System.out.println("DEG gk:" + gk);
                            // ヒープ構築
                            garg[2] = gk--;
                            garg[3] = NUM - 1;
                            gi = garg[2];
                            gflag = true;
                            break;
                        }
                        if (gj > 0) {
                            System.out.println("DEG gj:" + gj);
                            swap(a, 0, gj);
                            garg[2] = 0;
                            garg[3] = gj - 1;
                            gi = garg[2];
                            gflag = true; // 次回ヒープ整列起動
                            //popHeap(a, 0, gi-1)してる
                            gj--;
                        } else return false;
                    }
                    break;
                case HEAP_CONSTRUCTION: // ヒープ構築
                    int ch;
                    // garg[2]=left,garg[3]=right,gi=parent
                    if (gi * 2 + 1 > garg[3]) {
                        // 子がなければ終了．初期化する
                        if (--garg[2] == -1) return false;
                        gi = garg[2];
                        break;
                    }
                    ch = (gi * 2 + 2 <= garg[3] && a[gi * 2 + 2] > a[gi * 2 + 1]) ? gi * 2 + 2 : gi * 2 + 1;
                    if (a[gi] >= a[ch]) {
                        if (--garg[2] == -1) return false;
                        gi = garg[2];
                        break;
                    }
                    swap(a, gi, ch);
                    gi = ch;
                    break;
            }
        } else { // 降順
            switch (sort_s) {
                case BUBBLE_SORT:    // バブルソート１つ
                    if (a[gj] > a[gj - 1]) {
                        swap(a, gj, gj - 1);
                    }
                    gj--;
                    if (gj <= gi) {
                        gi++;
                        gj = NUM - 1;
                    }
                    if (gi >= NUM - 1) return false;
                    break;
                case SELECTION_SORT:    // セレクションソート１つ
                    if (a[gj] > a[minj]) {
                        minj = gj;
                    }
                    gj++;
                    if (gj >= NUM) {
                        swap(a, minj, gi);
                        gi++;
                        minj = gi;
                        gj = gi + 1;
                    }
                    if (gi >= NUM - 1) return false;
                    break;
                case INSERTION_SORT:    // インサーションソート１つ
                    if (a[gj] < a[gj + 1]) {
                        swap(a, gj, gj + 1);
                    } else {
                        gi++;
                        gj = gi + 0;
                    }    // マイナス１は直後にされるから、+0にしておくこと
                    gj--;
                    if (gj <= -1) {
                        gi++;
                        gj = gi - 1;
                    }
                    if (gi >= NUM) {
                        gi = 0;
                        return false;
                    }
                    break;
                case SHAKER_SORT:    // シェーカーソート１つ
                    // gkが左、glが右端
                    if (gi % 2 == 0) {
                        if (a[gj] > a[gj - 1]) {    // 交換
                            swap(a, gj, gj - 1);
                            count = gj;
                        }
                        gj--;
                        if (gj <= gk) {    // 反転
                            gi++;
                            gk = count;
                            gj = gk;    // 反転
                        }
                    } else {
                        if (a[gj] < a[gj + 1]) {    // 交換
                            swap(a, gj, gj + 1);
                            count = gj;
                        }
                        gj++;
                        if (gj >= gl) {
                            gi++;
                            gl = count;
                            gj = gl;
                        }
                    }
                    if (gk >= gl) return false;
                    break;
                case SHELL_SORT:    // シェルソート１つ
                    if (a[gj] < a[gj + gap[gk]]) {
                        swap(a, gj, gj + gap[gk]);
                    } else {
                        gi += 1/*gap[gk]*/;
                        gj = gi + 0;
                    }    // マイナス１は直後にされるから、+0にしておくこと
                    gj -= gap[gk];
                    if (gj <= -1) {
                        gi += 1/*gap[gk]*/;
                        gj = gi - gap[gk];
                    }
                    if (gi >= NUM) {
                        gk--;
                        if (gk == -1) {
                            gk = 0;
                            return false;
                        }
                        gi = gap[gk];
                        gj = gi - gap[gk];
                    }
                    break;
                case COMB_SORT:        // コムソート１つ
                    if (a[gi] < a[gi + gk]) {
                        swap(a, gi, gi + gk);
                        gflag = false;
                    }
                    gi++;
                    if (gi + gk >= NUM) {
                        if (gk == 1 && gflag) return false;
                        gi = 0;
                        gk = gk * 10 / 13;
                        gflag = true;
                    }
                    if (gk <= 1) gk = 1;
                    break;
                case ODDEVEN_SORT:
                    if (gi % 2 == 0) {
                        if (a[gj] < a[gj + 1]) {
                            swap(a, gj, gj + 1);
                            gflag = true;
                        }
                        gj += 2;
                        if (gj >= NUM - 1) {
                            gi++;
                            gj = 1;
                            gflag = false;
                        }
                    } else {
                        if (a[gj] < a[gj + 1]) {
                            swap(a, gj, gj + 1);
                            gflag = true;
                        }
                        gj += 2;
                        if (gj >= NUM - 1) {
                            if (!gflag) return false;
                            gi++;
                            gj = 0;
                            gflag = false;
                        }

                    }
                    break;
                case NANTTYATTE_SELECTION_SORT: // 降順なんちゃって選択
                    if (a[gj] > a[gi]) {
                        swap(a, gi, gj);
                    }
                    gj++;
                    if (gj >= NUM) {
                        gi++;
                        gj = gi + 1;
                    }
                    if (gi >= NUM - 1) {
                        gi = 0;
                        return false;
                    }
                    break;
                case HEAP_SORT: // 昇順ヒープ(簡略版)
                    if (gi > 0) {
                        swap(a, 0, gi);
                        descDownHeap(a, 0, gi - 1);
                        gi--;
                    } else return false;
                    break;
                case HEAP_SORT_FULL:
                    if (gflag) {
                        // ヒープ構築
                        System.out.println("constract..." + garg[2] + " " + gi);
                        int ch;
                        // garg[2]=left,garg[3]=right,gi=parentとする
                        if (gi * 2 + 1 > garg[3]) {
                            // 子がなければ終了．初期化する
                            gflag = false;
                            break;
                        }
                        ch = (gi * 2 + 2 <= garg[3] && a[gi * 2 + 2] < a[gi * 2 + 1]) ? gi * 2 + 2 : gi * 2 + 1;
                        if (a[gi] <= a[ch]) {
                            gflag = false;
                            break;
                        }
                        swap(a, gi, ch);
                        gi = ch;
                    } else { // 順次スワップ
                        if (gk > -1) {
                            System.out.println("DEG gk:" + gk);
                            // ヒープ構築
                            garg[2] = gk--;
                            garg[3] = NUM - 1;
                            gi = garg[2];
                            gflag = true;
                            break;
                        }
                        if (gj > 0) {
                            System.out.println("DEG gj:" + gj);
                            swap(a, 0, gj);
                            garg[2] = 0;
                            garg[3] = gj - 1;
                            gi = garg[2];
                            gflag = true; // 次回ヒープ整列起動
                            //popHeap(a, 0, gi-1)してる
                            gj--;
                        } else return false;
                    }
                    break;
                case HEAP_CONSTRUCTION:
                    int ch;
                    // garg[2]=left,garg[3]=right,gi=parent
                    if (gi * 2 + 1 > garg[3]) {
                        // 子がなければ終了．初期化する
                        if (--garg[2] == -1) return false;
                        gi = garg[2];
                        break;
                    }
                    ch = (gi * 2 + 2 <= garg[3] && a[gi * 2 + 2] < a[gi * 2 + 1]) ? gi * 2 + 2 : gi * 2 + 1;
                    if (a[gi] <= a[ch]) {
                        if (--garg[2] == -1) return false;
                        gi = garg[2];
                        break;
                    }
                    swap(a, gi, ch);
                    gi = ch;
                    break;
            }

        }
        return true;
    }

    // 一斉ソート
    public void all_sort(boolean order) {
        swap_counter = 0;
        if (order) {
            // 降順
            switch (sort_s) {
                case BUBBLE_SORT: // 昇順バブル
                    for (int i = 0; i < NUM; i++) {
                        for (int j = NUM - 1; j > i; j--) {
                            if (a[j] < a[j - 1]) {
                                swap(a, j, j - 1);
                            }
                        }
                    }
                    break;
                case SELECTION_SORT: // 昇順選択
                    for (int i = 0; i < NUM - 1; i++) {
                        int minj = i;
                        for (int j = i + 1; j < NUM; j++) {
                            if (a[minj] > a[j]) minj = j;
                        }
                        swap(a, i, minj);
                    }

                    break;
                case INSERTION_SORT: // 昇順挿入
                    for (int i = 1; i < NUM; i++) {
                        int k = a[i];
                        int j = i;
                        while (j > 0) {
                            if (a[j - 1] > k) {
                                a[j] = a[j - 1];
                                swap_counter++;
                            } else break;
                            j--;
                        }
                        a[j] = k;
                    }
                    break;
                case SHELL_SORT: // 昇順シェル
                    int g;
                    for (g = 0; gap[g] < NUM; g++) ;
                    while (true) {
                        for (int i = gap[g]; i < NUM; i++) {
                            int k = a[i];
                            int j = i;
                            while (j - gap[g] >= 0) {
                                if (a[j - gap[g]] > k) {
                                    a[j] = a[j - gap[g]];
                                    swap_counter++;
                                } else break;
                                j -= gap[g];
                            }
                            a[j] = k;
                        }
                        g--;
                        if (g == -1) break;
                    }
                    break;
                case COMB_SORT: // 昇順コム
                    int h = NUM * 10 / 13;    // ギャップ
                    boolean f = true;
                    while (f) {
                        if (h == 1) f = false;
                        for (int i = 0; i + h < NUM; i++) {
                            if (a[i] > a[i + h]) {
                                swap(a, i, i + h);
                                f = true;
                            }
                        }
                        h = h * 10 / 13;
                        if (h <= 1) h = 1;
                    }
                    break;
                case ODDEVEN_SORT: // 昇順奇偶
                    boolean f1 = true;
                    while (f1) {
                        f1 = false;
                        for (int i = 0; i < NUM - 1; i += 2) {
                            if (a[i] > a[i + 1]) {
                                swap(a, i, i + 1);
                                f1 = true;
                            }
                        }
                        for (int i = 1; i < NUM - 1; i += 2) {
                            if (a[i] > a[i + 1]) {
                                swap(a, i, i + 1);
                                f1 = true;
                            }
                        }
                    }
                    break;
                case NANTTYATTE_SELECTION_SORT: // 昇順なんちゃって選択
                    for (int i = 0; i < NUM; i++) {
                        for (int j = i + 1; j < NUM; j++) {
                            if (a[i] > a[j]) {
                                swap(a, i, j);
                            }
                        }
                    }
                    break;
                case HEAP_SORT: // 昇順ヒープ
                case HEAP_SORT_FULL:
                    for (int i = (NUM - 1) / 2; i >= 0; i--) ascDownHeap(a, i, NUM - 1);
                    for (int i = NUM - 1; i > 0; i--) {
                        swap(a, 0, i);
                        ascDownHeap(a, 0, i - 1);
                    }
                    break;
                case HEAP_CONSTRUCTION: // 昇順ヒープ構築
                    for (int i = (NUM - 1) / 2; i >= 0; i--) ascDownHeap(a, i, NUM - 1);
                    break;
            }    // スイッチエンド
        } else { // 降順
            switch (sort_s) {
                case BUBBLE_SORT: // 降順バブル
                    for (int i = 0; i < NUM; i++) {
                        for (int j = NUM - 1; j > i; j--) {
                            if (a[j] > a[j - 1]) {
                                swap(a, j, j - 1);
                            }
                        }
                    }
                    break;
                case SELECTION_SORT: // 降順選択
                    for (int i = 0; i < NUM - 1; i++) {
                        int minj = i;
                        for (int j = i + 1; j < NUM; j++) {
                            if (a[minj] < a[j]) minj = j;
                        }
                        swap(a, i, minj);
                    }
                    break;
                case INSERTION_SORT: // 降順挿入
                    for (int i = 1; i < NUM; i++) {
                        int k = a[i];
                        int j = i;
                        while (j > 0) {
                            if (a[j - 1] < k) {
                                a[j] = a[j - 1];
                                swap_counter++;
                            } else break;
                            j--;
                        }
                        a[j] = k;
                    }
                    break;
                case SHELL_SORT: // 降順シェル
                    int g;
                    for (g = 0; gap[g] < NUM; g++) ;
                    while (true) {
                        for (int i = gap[g]; i < NUM; i++) {
                            int k = a[i];
                            int j = i;
                            while (j - gap[g] >= 0) {
                                if (a[j - gap[g]] < k) {
                                    a[j] = a[j - gap[g]];
                                    swap_counter++;
                                } else break;
                                j -= gap[g];
                            }
                            a[j] = k;
                        }
                        g--;
                        if (g == -1) break;
                    }
                    break;
                case COMB_SORT: // 降順コム
                    int h = NUM * 10 / 13;    // ギャップ
                    boolean f = true;
                    while (f) {
                        if (h == 1) f = false;
                        for (int i = 0; i + h < NUM; i++) {
                            if (a[i] < a[i + h]) {
                                swap(a, i, i + h);
                                f = true;
                            }
                        }
                        h = h * 10 / 13;
                        if (h <= 1) h = 1;
                    }
                    break;
                case ODDEVEN_SORT: // 降順奇偶
                    boolean f1 = true;
                    while (f1) {
                        f1 = false;
                        for (int i = 0; i < NUM - 1; i += 2) {
                            if (a[i] < a[i + 1]) {
                                swap(a, i, i + 1);
                                f1 = true;
                            }
                        }
                        for (int i = 1; i < NUM - 1; i += 2) {
                            if (a[i] < a[i + 1]) {
                                swap(a, i, i + 1);
                                f1 = true;
                            }
                        }
                    }
                    break;
                case NANTTYATTE_SELECTION_SORT: // 降順なんちゃって選択
                    for (int i = 0; i < NUM; i++) {
                        for (int j = i + 1; j < NUM; j++) {
                            if (a[i] > a[j]) {
                                swap(a, i, j);
                            }
                        }
                    }
                    break;
                case HEAP_SORT: // 降順ヒープ
                case HEAP_SORT_FULL:
                    // 末端からヒープ構築．(NUM-1)/2以後は葉なので意味なしなため省略
                    for (int i = (NUM - 1) / 2; i >= 0; i--) descDownHeap(a, i, NUM - 1);
                    for (int i = NUM - 1; i > 0; i--) {
                        swap(a, 0, i);
                        descDownHeap(a, 0, i - 1); // 末端が確定したため，それを除いて天頂を下へ
                    }
                    break;
                case HEAP_CONSTRUCTION:
                    for (int i = (NUM - 1) / 2; i >= 0; i--) descDownHeap(a, i, NUM - 1);
                    break;
            }    // スイッチエンド

        }
        System.out.println("交換回数：" + swap_counter + "回");
    }


    public void sorting_network_init(Graphics g) {
        int offset = 150;
        int xs = 10;
        int csize = 8;
        int xe = MainPanel.WIDTH - 50;
        int y = (MainPanel.HEIGHT - offset) / NUM - 10;
        int times = 0;
        for (int i = 0; i < NUM; i++) {
            g.drawLine(xs, y * i + offset, xe, y * i + offset);
        }

        for (int i = 0; i < NUM; i++) {
            for (int j = NUM - 1; j > i; j--) {
                //int w=(NUM-1)*NUM/2*(k+1);
                int w = 16 * (times + 1);

                g.drawLine(w, y * (j - 1) + offset, w, y * j + offset);
                g.fillOval(w - csize / 2, y * j + offset - csize / 2, csize, csize);
                g.fillOval(w - csize / 2, y * (j - 1) + offset - csize / 2, csize, csize);

                times++;
            }
        }

    }

    // 特殊なタイプに対応
    public void graph_sorting(int s, int amount) {
        // s:セレクト　amount:分割数

        int hosei = 0;
        int cycle = NUM / amount;
        int bairitu = 2;

        switch (s) {
            case STAIRS:    // 階段状に戻す
                for (int i = 0; i < NUM; i++) {
                    a[i] = i;
                }
                break;
            case NEEDLE_MOUNTAIN:// 安定針山
                allSortp(true);    // シェルソートで整列
                for (int i = 0, k = 0; i < amount * 2; i++) {    // 山型に変形していく
                    for (int j = i; j < NUM; j += amount, k++) {
                        worka[k] = a[j];
                    }

                    for (int j = NUM - 1 - i; j >= 0; j -= amount, k++) {
                        worka[k] = a[j];
                    }
                }

                for (int i = 0; i < NUM; i++) {
                    a[i] = worka[i];
                }

                break;
            case SAWTOOTH_WAVE:    // 中間安定のこぎり波
                int k = 0;
                allSortp(true);
                for (int i = 0; i < amount; i++) {
                    for (int j = i; j < NUM; j += amount, k++) {
                        worka[k] = a[j];
                    }
                }
                for (int i = 0; i < NUM; i++) {
                    a[i] = worka[i];
                }
                break;
            case ALTERNATEII:    // 安定昇降
                allSortp(true);
                for (int i = 1; i < NUM / 2; i += 2) {
                    swap(a, i, NUM - i);
                }
                break;
            case SINE_CURVE:    // 正弦波
                for (int i = 0; i < NUM; i++) {
                    a[i] = (int) (Math.sin((double) Math.PI * (i - cycle / 2) / cycle) * 100) / bairitu + 50 + hosei;
                }
                break;
            case COSINE_CURVE:    // 余弦波
                for (int i = 0; i < NUM; i++) {
                    a[i] = (int) (Math.cos((double) Math.PI * (i) / cycle) * 100) / bairitu + 50 + hosei;
                }
                break;
            case PULSE: // パルス
                int t = 0;
                while (true) {
                    for (int i = 0; i < amount && t < NUM; i++, t++) {
                        a[t] = NUM - 1;
                    }
                    for (int i = 0; i < amount && t < NUM; i++, t++) {
                        a[t] = 1 - 1;
                    }
                    if (t == NUM) break;
                }
                break;
            case DENSE: // 範囲密
                int D = NUM / 2;
                for (int i = 0; i < NUM; i++) {
                    a[i] = RANDOM(D - amount, D);
                }
                break;
            case DISCRETE: // 離散値
                for (int i = 0; i < NUM; i++) {
                    a[i] = (RANDOM(0, NUM - 1) % amount) * (NUM / 10);
                }
        }
    }

    public void draw(Graphics g, int s) {
        switch (s) {
            case 0:    // 点グラフ
                for (int i = 0; i < NUM; i++) {
                    if (emphasis(i, sort_s)) g.setColor(Color.RED);
                    else if (commitedEmphasis(i, sort_s)) g.setColor(Color.BLUE);
                    else g.setColor(Color.GRAY);
                    g.fillRect((SIZE + MARGIN) * (i + 1) - SIZE, MainPanel.HEIGHT - 100 - block[a[i]].getHeight(), SIZE, SIZE);
                }
                break;
            case 1:// 折れ線グラフ
                g.setColor(Color.RED);
                for (int i = 0; i < NUM - 1; i++) {
                    g.drawLine((SIZE + MARGIN) * (i + 2) - SIZE / 2, MainPanel.HEIGHT - 100 - block[a[i + 1]].getHeight(), (SIZE + MARGIN) * (i + 1) - SIZE / 2, MainPanel.HEIGHT - 100 - block[a[i]].getHeight());
                }
                break;
            case 2:    // 棒グラフ
                for (int i = 0; i < NUM; i++) {
                    if (emphasis(i, sort_s)) g.setColor(Color.RED);
                    else if (commitedEmphasis(i, sort_s)) g.setColor(Color.BLUE);
                    else if (targetEmphasis(i, sort_s)) g.setColor(Color.GREEN);
                    else g.setColor(Color.GRAY);
                    block[a[i]].draw(g, (SIZE + MARGIN) * (i + 1) - SIZE);
                }
                break;
    		/*
    	case 3: // 二分木グラフ
    		// MainPanelでやらないとできない
    		break;
    		*/
        }
    }

    public void vdraw(Graphics g, int s) {
        switch (sort_s) {
            case BUBBLE_SORT:
                varDraw(g, gi, Color.ORANGE);
                varDraw(g, gj, Color.RED);
                varDraw(g, gj - 1, Color.RED);
                break;
            case SELECTION_SORT:
                varDraw(g, gi, Color.ORANGE);
                varDraw(g, gj, Color.ORANGE);
                break;
            case INSERTION_SORT:
                varDraw(g, gi, Color.ORANGE);
                varDraw(g, gj, Color.RED);
                varDraw(g, gj + 1, Color.RED);
                break;
            case SHAKER_SORT:
                varDraw(g, gk, Color.BLUE);
                varDraw(g, gl, Color.ORANGE);
                if (gi % 2 == 0) {
                    varDraw(g, gj, Color.RED);
                    varDraw(g, gj - 1, Color.RED);
                } else {
                    varDraw(g, gj, Color.RED);
                    varDraw(g, gj + 1, Color.RED);
                }
                break;
            case SHELL_SORT:
                varDraw(g, gj, Color.ORANGE);
                varDraw(g, gj + gap[gk], Color.ORANGE);

                break;
            case COMB_SORT:
                varDraw(g, gi, Color.ORANGE);
                varDraw(g, gi + gk, Color.ORANGE);
                break;
            case ODDEVEN_SORT:
                varDraw(g, gj, Color.ORANGE);
                varDraw(g, gj + 1, Color.ORANGE);
                break;
            default:
                break;
        }
        //System.out.println("交換回数："+swap_counter+"回");
    }

    private void varDraw(Graphics g, int x, Color C) {
        g.setColor(C);
//    	g.fillRect((SIZE+MARGIN)*(x)+SIZE,MainPanel.HEIGHT-100+SIZE/2,SIZE,SIZE);
        g.drawLine((SIZE + MARGIN) * (x) + SIZE, MainPanel.HEIGHT - 100 + 2 * 5, (SIZE + MARGIN) * (x) + SIZE, 0);
    }

    private boolean emphasis(int i, int s) {
        // red emphasis
        // 比較(不動)のもの
        switch (s) {
            case BUBBLE_SORT:
                if (i == gj || i == gj - 1) return true;
                break;
            case SELECTION_SORT:
                if (i == minj || i == gj) return true;
                break;
            case INSERTION_SORT:
                if (i == gj || i == gj + 1) return true;
                break;
            case SHAKER_SORT:
                if (i == gk || i == gl) return true;
                break;
            case SHELL_SORT:
                //if(i==gj||i==gj+gap[gk])return true;
                if (i == gj + gap[gk]) return true;
                break;
            case COMB_SORT:
                if (i == gi || i == gi + gk) return true;
                break;
            case ODDEVEN_SORT:
                if (i == gj || i == gj + 1) return true;
                break;
            case HEAP_SORT:
                if (i == gi - 1) return true;
                break;
            case HEAP_SORT_FULL:
                if (gflag && i == gi) return true;
                if (!gflag && i == gj - 1) return true;
                break;
            case HEAP_CONSTRUCTION:
                if (i == gi) return true;
                break;
        }
        return false;
    }

    private boolean targetEmphasis(int i, int s) {
        // green
        // 比較(可変)の強調
        switch (s) {
            case BUBBLE_SORT:
                if (i == gj || i == gj - 1) return true;
                break;
            case SELECTION_SORT:
                if (i == minj || i == gj) return true;
                break;
            case INSERTION_SORT:
                if (i == gj || i == gj + 1) return true;
                break;
            case SHAKER_SORT:
                if (i == gk || i == gl) return true;
                break;
            case SHELL_SORT:
                if (i == gj) return true;
                break;
            case COMB_SORT:
                if (i == gi || i == gi + gk) return true;
                break;
            case ODDEVEN_SORT:
                if (i == gj || i == gj + 1) return true;
                break;
            case HEAP_SORT:
                if (i == gi) return true;
                break;
            case HEAP_SORT_FULL:
                if (gflag && (gi * 2 + 1 < gj) && (i == gi * 2 + 1)) return true;
                else if (gflag && (gi * 2 + 2 < gj) && (i == gi * 2 + 2)) return true;
                else if (!gflag && (i == gj)) return true;
                break;
            case HEAP_CONSTRUCTION:
                if ((gi * 2 + 1 < NUM) && (i == gi * 2 + 1)) return true;
                else if ((gi * 2 + 2 < NUM) && (i == gi * 2 + 2)) return true;
                break;
        }
        return false;
    }

    private boolean commitedEmphasis(int i, int s) {
        // blue
        // 確定した領域の強調
        switch (s) {
            case BUBBLE_SORT:
                //if(ascending_order&&i<gi) return true;
                //else if(!ascending_order&&i<gi) return true;
                if (i < gi) return true;
                break;
            case SELECTION_SORT:
                if (i < gi) return true;
                break;
            case INSERTION_SORT:
                // 確定ソートではない
                //if(i==gj||i==gj+1) return true;
                break;
            case SHAKER_SORT:
                if (i < gk || i > gl) return true;
                break;
            case SHELL_SORT:
                //if(i==gj)return true;
                break;
            case COMB_SORT:
                //if(i==gi||i==gi+gk) return true;
                break;
            case ODDEVEN_SORT:
                //if(i==gj||i==gj+1) return true;
                break;
            case HEAP_SORT:
                if (i > gi) return true;
                break;
            case HEAP_SORT_FULL:
                if (i > gj) return true;
                break;
            case HEAP_CONSTRUCTION:
                //if(i==gi*2+1||i==gi*2+2)return true;
                break;
        }
        return false;
    }


    private int RANDOM(int MIN, int MAX) {
        Random rnd = new Random();
        int r = rnd.nextInt(NUM);
        return ((MIN) + (int) (r / (float) NUM * ((MAX) - (MIN) + 1)));
    }

    private void swap(int array[], int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
        swap_counter++;
    }

    public double c() {    // 相関係数の計算関数
        int[] b = new int[NUM];
        for (int i = 0; i < NUM; i++) b[i] = i + 1;
        return calcCorr(NUM, a, b);
    }

    private double calcCorr(int n, int[] x, int[] y)    // 相関係数の計算
    {
        double mx = calcMean(n, x);
        double my = calcMean(n, y);
        double xx = 0, yy = 0, xy = 0;
        for (int i = 0; i < n; i++) xx += Math.pow(x[i] - mx, 2);
        xx = Math.sqrt(xx);
        for (int i = 0; i < n; i++) yy += Math.pow(y[i] - my, 2);
        yy = Math.sqrt(yy);
        for (int i = 0; i < n; i++) xy += (x[i] - mx) * (y[i] - my);
        return (xy / (xx * yy));
    }

    // calc mean
    private double calcMean(int n, int[] a)
    // シェルソートで整列してしまう
    {
        double mean = 0;
        for (int i = 0; i < n; i++) mean += a[i];
        mean /= (double) n;
        return mean;
    }

    private void ascDownHeap(int[] a, int left, int right) {
        // 昇順ヒープ挿入
        int parent = left;
        int child;
        int temp = a[parent];
        while (parent * 2 + 1 <= right) {
            child = (parent * 2 + 2 <= right && a[parent * 2 + 2] > a[parent * 2 + 1]) ? parent * 2 + 2 : parent * 2 + 1;
            swap_counter++;
            if (temp >= a[child]) break;
            // break時点でparent化してない
            a[(child - 1) / 2] = a[child];
            parent = child;
        }
        a[parent] = temp;
    }

    private void descDownHeap(int[] a, int left, int right) {
        // 降順ヒープ挿入
        int parent = left;
        int child;
        int temp = a[parent];
        while (parent * 2 + 1 <= right) {
            child = (parent * 2 + 2 <= right && a[parent * 2 + 2] < a[parent * 2 + 1]) ? parent * 2 + 2 : parent * 2 + 1;
            swap_counter++;
            if (temp <= a[child]) break;
            // break時点でparent化してない
            a[(child - 1) / 2] = a[child];
            parent = child;
        }
        a[parent] = temp;
    }

    private void allSortp(boolean order) {
        if (order) {
            int g;
            for (g = 0; gap[g] < NUM; g++) ;
            while (true) {
                for (int i = gap[g]; i < NUM; i++) {
                    int k = a[i];
                    int j = i;
                    while (j - gap[g] >= 0) {
                        if (a[j - gap[g]] > k) {
                            a[j] = a[j - gap[g]];
                            swap_counter++;
                        } else break;
                        j -= gap[g];
                    }
                    a[j] = k;
                }
                g--;
                if (g == -1) break;
            }
        } else {
            int g;
            for (g = 0; gap[g] < NUM; g++) ;
            while (true) {
                for (int i = gap[g]; i < NUM; i++) {
                    int k = a[i];
                    int j = i;
                    while (j - gap[g] >= 0) {
                        if (a[j - gap[g]] < k) {
                            a[j] = a[j - gap[g]];
                            swap_counter++;
                        } else break;
                        j -= gap[g];
                    }
                    a[j] = k;
                }
                g--;
                if (g == -1) break;
            }

        }
    }

    public void generateBintreeP() {
        // 配列二分木形式でdotファイル変形
        generateBintree(a);
    }

    private void generateBintree(int[] a) {
        // 二分木生成．
        try {
            FileWriter fw = new FileWriter("temp.dot", false); // 追記
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            generateDotBegin(pw, a); // 固定ルールを書き込む
            pw.println("nodesep=0.2;");
            ArrayList<ArrayList<Integer>> alist = new ArrayList<ArrayList<Integer>>();
            dfs(alist, a, 0, 0); // 深さ優先
            // 深さ優先探索で進み，それぞれの葉の深さを見つけて順番にリストにぶち込む
            // それぞれをrank=sameで等階級にする
            // index*2+1と，index*2+2を次のノードとし，index=0を根とする二分木を構築する．
            for (int i = 0; i < alist.size(); i++) {
                // 同等ランク
                pw.print("{rank=same;");
                for (int j = 0; j < alist.get(i).size(); j++) {
                    pw.print("\"" + a[alist.get(i).get(j)] + "\";");
                }
                pw.println("}");
            }
            for (int i = 0; i < alist.size(); i++) {
                if (alist.get(i).size() <= 1) continue;
                for (int j = 0; j < alist.get(i).size(); j++) {
                    if (j != 0) pw.print("--\"" + a[alist.get(i).get(j)] + "\"");
                    else pw.print("\"" + a[alist.get(i).get(j)] + "\"");
                }
                pw.println("[style=invis];");
            }
            // 強調する
            for (int i = 0; i < NUM; i++) {
                if (emphasis(i, sort_s))
                    pw.println("\"" + a[i] + "\" [fillcolor=\"red\",style=\"solid,filled\"] \"" + a[i] + "\";");
                else if (targetEmphasis(i, sort_s))
                    pw.println("\"" + a[i] + "\" [fillcolor=\"green\",style=\"solid,filled\"] \"" + a[i] + "\";");
                else if (commitedEmphasis(i, sort_s))
                    pw.println("\"" + a[i] + "\" [fillcolor=\"blue\",style=\"solid,filled\"] \"" + a[i] + "\";");
                if (i * 2 + 1 < NUM) pw.println("\"" + a[i] + "\"--\"" + a[i * 2 + 1] + "\";");

                if (i * 2 + 2 < NUM) pw.println("\"" + a[i] + "\"--\"" + a[i * 2 + 2] + "\";");
            }
            pw.println("}"); // 終了
            pw.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tjpg temp.dot -o a.jpg");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void dfs(ArrayList<ArrayList<Integer>> Al, int[] a, int index, int depth) {
        if (Al.size() <= depth) {
            // 対応するアレイがない場合は，アレイ追加
            Al.add(new ArrayList<Integer>());
        }
        Al.get(depth).add(index);
        if (index * 2 + 1 < NUM) dfs(Al, a, index * 2 + 1, depth + 1);
        if (index * 2 + 2 < NUM) dfs(Al, a, index * 2 + 2, depth + 1);
    }

    private void generatePointerGraph(int[] a) {
        // ポインタ付グラフ生成
    }

    private void generateDotBegin(PrintWriter pw, int[] a) {
        /* 返り値をFILE型にしたい */
        // graphviz用dotファイル生成
        // 二分木での生成 0--1 0--2 1--3 1--4をforでやる
        pw.println("graph g{");
        pw.println("layout=dot;");
        pw.println("graph[charset=\"UTF-8\",fontname=\"meiryo\",label=\"Binary Tree\"];");
        pw.println("node[shape=circle,size=10,fontname=\"meiryo\",fontsize=36,color=\"black\"];");
        pw.println("edge[fontname=\"meiryo\"];");
    }

}
