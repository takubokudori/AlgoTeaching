package algoteach;

import java.awt.Color;
import java.awt.Graphics;

public class Block {
    private int X;
    private int Y;
    private int WIDTH;
    private int HEIGHT;

    Block(int x, int y, int width, int height) {
        X = x;
        Y = y;
        WIDTH = width;
        HEIGHT = height;
    }

    int getHeight() {
        // 大きさを返す
        return HEIGHT;
    }

    void draw(Graphics g) {
        // 単純出力
        g.setColor(Color.GRAY);
        g.fillRect(X, Y, WIDTH, HEIGHT);
        g.setColor(Color.BLUE);
        g.drawRect(X, Y, WIDTH, HEIGHT);
    }

    void draw(Graphics g, int x) {
        // 座標と共に表示する場合
        //g.setColor(Color.GRAY);
        // 実体
        g.fillRect(x, Y, WIDTH, HEIGHT);
        g.setColor(Color.BLUE);    // 外枠は青色
        g.drawRect(x, Y, WIDTH, HEIGHT);

    }
}
