package algoteach;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AlgoTeaching extends JFrame implements ActionListener {
    /**
     *
     */
    static JMenuBar menubar = new JMenuBar();
    static JMenu[] menu = new JMenu[MainPanel.MENU_NUM];
    static JMenuItem[] sortmenu = new JMenuItem[MainPanel.SORTING_NUM];
    static JMenuItem[] zukeimenu = new JMenuItem[MainPanel.ZUKEING_NUM];
    static JMenuItem[] displaymenu = new JMenuItem[MainPanel.DISPLAY_FORMAT];
    static JMenuItem[] optionmenu = new JMenuItem[MainPanel.OPTION_NUM];
    static MainPanel panel = new MainPanel();

    public AlgoTeaching() {
        // タイトルを設定

        // コンストラクタ
        setTitle("ソートアルゴリ君");

        // メインパネルを作成してフレームに追加
        // コンテントペイン追加
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());
        contentPane.add(panel, BorderLayout.CENTER);


        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Math.random();
        AlgoTeaching frame = new AlgoTeaching();
        for (int i = 0; i < MainPanel.MENU_NUM; i++) {
            menu[i] = new JMenu(MainPanel.menustr[i]);
            menubar.add(menu[i]);
        }

        for (int i = 0; i < MainPanel.SORTING_NUM; i++) {
            sortmenu[i] = new JMenuItem(MainPanel.sortstr[i]);
            sortmenu[i].addActionListener(frame);
            menu[0].add(sortmenu[i]);    // ソート種類
        }
        for (int i = 0; i < MainPanel.ZUKEING_NUM; i++) {
            zukeimenu[i] = new JMenuItem(MainPanel.zukeistr[i]);
            zukeimenu[i].addActionListener(frame);
            menu[1].add(zukeimenu[i]);    // 図形
        }
        for (int i = 0; i < MainPanel.DISPLAY_FORMAT; i++) {
            displaymenu[i] = new JMenuItem(MainPanel.displaystr[i]);
            displaymenu[i].addActionListener(frame);
            menu[2].add(displaymenu[i]);    // 図形
        }
        for (int i = 0; i < MainPanel.OPTION_NUM; i++) {
            optionmenu[i] = new JMenuItem(MainPanel.optionstr[i]);
            optionmenu[i].addActionListener(frame);
            menu[3].add(optionmenu[i]);    // 図形
        }
        frame.setSize(1500, 1500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setJMenuBar(menubar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < MainPanel.SORTING_NUM; i++) {
            if (e.getSource() == sortmenu[i]) {
                panel.setSortingSelect(i);
                break;
            }
        }
        for (int i = 0; i < MainPanel.ZUKEING_NUM; i++) {
            if (e.getSource() == zukeimenu[i]) {
                panel.setZukeingSelect(i);
                break;
            }
        }
        for (int i = 0; i < MainPanel.DISPLAY_FORMAT; i++) {
            if (e.getSource() == displaymenu[i]) {
                panel.changeDisplay(i);
                break;
            }
        }
        for (int i = 0; i < MainPanel.OPTION_NUM; i++) {
            if (e.getSource() == optionmenu[i]) {
                String value;
                switch (i) {
                    case 0:
                        value = JOptionPane.showInputDialog(this, "個数：半角数字で");
                        if (value != null) {
                            panel.setBlockNum(value);
                        }
                        break;
                    case 1:
                        value = JOptionPane.showInputDialog(this, "コード読取：スペース区切り半角数字で");
                        if (value != null) {
                            panel.setCode(value);
                        }
                        break;

                    case 2:
                        value = JOptionPane.showInputDialog(this, "待機時間(ミリ秒）：半角整数で");
                        if (value != null) {
                            panel.setWaitTime(value);
                        }
                        break;
                    case 3:
                        value = JOptionPane.showInputDialog(this, "図形倍率：半角整数で");
                        if (value != null) {
                            panel.setBairitu(value);
                        }
                        break;
                }
                break;
            }
        }
    }

}

class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
//	Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("a.jpg"));
    BufferedImage image = null;
    public static final int LNUM = 10;
    public static final int WIDTH = 1500;    // 大きさ
    public static final int HEIGHT = 1500;    // 高さ
    public static final int MENU_NUM = 4;
    public static final int DISPLAY_FORMAT = 4;    // 表示形式
    public static final int SORTING_NUM = 12;        // ソートの種類
    public static final int ZUKEING_NUM = 9;        // 図形種類
    public static final int OPTION_NUM = 4;
    private boolean running_state = false;        // スレッドが走っているか
    private boolean ascending_order = true;    // 昇降順
    boolean display[] = new boolean[DISPLAY_FORMAT];    // 各表示形式
    private int WAIT_TIME = 1;    // 待機時間
    private int sorting_select = 0;    // ソート数
    private int zukeing_select = 0;
    private int bairitu = 1;
    private SortingBlocks sblocks = new SortingBlocks();
    private JPanel[] panel = new JPanel[3];
    private JLabel debugl;
    // private JLabel graphl=new JLabel(icon);
    // private JLabel[] label=new JLabel[LNUM];
    private JTextField bntextf, inputtextf;
    private JCheckBox[] displaybox = new JCheckBox[DISPLAY_FORMAT];
    private JButton startb, randomb, orderb, printb;
    private JComboBox<String> sort_select, zukei_select;
    public static final String[] menustr = {"ソート種類", "図形", "表示形式", "設定変更"};
    public static final String[] displaystr = {"点グラフ", "線グラフ", "棒グラフ", "二分木グラフ"};
    public static final String[] sortstr = {"バブルソート", "選択ソート", "挿入ソート", "シェーカーソート", "シェルソート", "コムソート", "奇偶転置ソート", "なんちゃって選択ソート", "ヒープソート(構築省略)", "ヒープソート(完全版)", "クイックソート", "ヒープ構築"};
    //public static final String[] sortstr={"ソート１","ソート２","ソート３","ソート４","ソート５","ソート６","ソート７","ソート８"};
    public static final String[] zukeistr = {"階段", "針山", "のこぎり波", "大小交互", "正弦波", "余弦波", "パルス波", "範囲密", "離散値"};
    public static final String[] optionstr = {"個数変更", "コード読込", "待ち時間変更", "図形倍率変更"};

    public MainPanel() {
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new FlowLayout());
        panel[0] = new JPanel(new GridLayout());
        for (int i = 0; i < DISPLAY_FORMAT; i++) {
            if (i == 2) {
                // 起動時から表示有
                displaybox[i] = new JCheckBox(displaystr[i], true);
                display[i] = true;
            } else {
                displaybox[i] = new JCheckBox(displaystr[i]);
                display[i] = false;
            }
            panel[0].add(displaybox[i], BorderLayout.EAST);
            displaybox[i].addItemListener(new ilis());
        }

        try {
            image = ImageIO.read(new File("a.jpg"));
        } catch (Exception ex) {
            //ex.printStackTrace();
            image = null;
        }

        debugl = new JLabel("交換回数：0");
        sort_select = new JComboBox<String>(sortstr);
        zukei_select = new JComboBox<String>(zukeistr);
        startb = new JButton("実行");
        randomb = new JButton("ランダム");
        orderb = new JButton("昇順");
        bntextf = new JTextField();
        inputtextf = new JTextField();
        printb = new JButton("出力");
        bntextf.setPreferredSize(new Dimension(100, 20));    // 個数
        inputtextf.setPreferredSize(new Dimension(100, 20));    // 自主的入力
        add(panel[0], BorderLayout.EAST);
        add(debugl, BorderLayout.SOUTH);
        //add(graphl,BorderLayout.NORTH);
        add(startb, BorderLayout.SOUTH);
        add(randomb, BorderLayout.SOUTH);
        add(orderb, BorderLayout.NORTH);
        add(sort_select, BorderLayout.NORTH);
        add(zukei_select, BorderLayout.NORTH);
        add(bntextf, BorderLayout.NORTH);
        add(inputtextf, BorderLayout.NORTH);
        add(printb, BorderLayout.SOUTH);
        sort_select.addActionListener(new alis());
        zukei_select.addActionListener(new alis());
        bntextf.addActionListener(new alis());
        inputtextf.addActionListener(new alis());
        startb.addActionListener(new alis());
        randomb.addActionListener(new alis());
        orderb.addActionListener(new alis());
        printb.addActionListener(new alis());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //this.setFocusable(true);
        //requestFocusInWindow();	// フォーカスを戻す


        sblocks.vdraw(g, sorting_select);
        //sblocks.sorting_network_init(g);
        for (int i = 0; i < DISPLAY_FORMAT; i++) {
            if (display[i]) sblocks.draw(g, i);
        }
        if (display[3]) {
            sblocks.generateBintreeP();
            try {
                image = ImageIO.read(new File("a.jpg"));
            } catch (Exception ex) {
                ex.printStackTrace();
                image = null;
            }
            // tracker使うからMainPanelクラスじゃないとできない?
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (image != null)
                g.drawImage(image, WIDTH / 2 - image.getWidth() / 2, HEIGHT / 2 - image.getHeight() / 2, this);
        }
    }

    public void run() {
        // 交換アルゴリズム
        boolean breakflag = false;
        //repaint();
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        while (running_state = sblocks.sort(sorting_select)) {
            System.out.println("相関係数：" + sblocks.c());
            try {
                repaint();
            } catch (RuntimeException e) {
                System.out.println("エラー");
            }
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!running_state) {
                breakflag = true;    // ボタンとかで中断された場合
                break;
            }
            debugl.setText("交換回数：" + sblocks.getSwapCounter());
        }
        long stop = System.currentTimeMillis();
        if (breakflag) {
            System.out.println("THREAD WAS STOPPED");
        } else {
            System.out.println("THREAD ENDED");
        }
        debugl.setText("交換回数：" + sblocks.getSwapCounter());
        startb.setText("実行");
        System.out.println("交換回数：" + sblocks.getSwapCounter());
        System.out.println("実行時間：" + (stop - start) + "ミリ秒");
        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:    // ソートが上
                sorting_select++;
                sorting_select %= SORTING_NUM;
                sort_select.setSelectedIndex(sorting_select);
                repaint();
                System.out.println(sorting_select);
                break;
            case KeyEvent.VK_DOWN:    // ソートが下
                sorting_select--;
                if (sorting_select < 0) sorting_select = SORTING_NUM - 1;
                sort_select.setSelectedIndex(sorting_select);
                repaint();
                System.out.println(sorting_select);
                break;
            case KeyEvent.VK_A:    // 開始
                if (!running_state) threadStart();
                else running_state = false;
                break;
            case KeyEvent.VK_B:    // 表示切替
                displaybox[sorting_select].setSelected(!display[sorting_select]);
                repaint();
                break;
            case KeyEvent.VK_C:    // 一斉ソート
                System.out.println("ソート種類：" + sortstr[sorting_select]);
                sblocks.ijinit(sorting_select, ascending_order);
                long start = System.currentTimeMillis();
                sblocks.all_sort(ascending_order);
                long stop = System.currentTimeMillis();
                debugl.setText("交換回数：" + sblocks.getSwapCounter());
                System.out.println("実行時間：" + (stop - start) + "ミリ秒");
                repaint();
                break;
            case KeyEvent.VK_D:    // 図形の種類選択
                zukeing_select++;
                zukeing_select %= ZUKEING_NUM;
                zukei_select.setSelectedIndex(zukeing_select);
                break;
            case KeyEvent.VK_R:    // ランダム
                sblocks.randomSort();
                repaint();
                break;
            case KeyEvent.VK_O:    // オーダー反転
                reverseAscending_order();
                break;
            case KeyEvent.VK_G: // ジェネレートする
                sblocks.generateBintreeP();
                repaint();
                break;
            case KeyEvent.VK_P:    // 現在の数値状態をプリント
                inputtextf.setText(sblocks.getText());
                break;
            case KeyEvent.VK_H:    // 図形反映
                sblocks.graph_sorting(zukeing_select, bairitu);
                repaint();
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }

    class ilis implements ItemListener {
        // チェックボックスのイベントリスナ
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JCheckBox tmp = (JCheckBox) e.getSource();// 発生源取得
                for (int i = 0; i < DISPLAY_FORMAT; i++) {
                    if (tmp.getText() == displaystr[i]) {
                        display[i] = true;
                    }
                }
                repaint();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                JCheckBox tmp = (JCheckBox) e.getSource();
                for (int i = 0; i < DISPLAY_FORMAT; i++) {
                    if (tmp.getText() == displaystr[i]) {
                        display[i] = false;
                    }
                    repaint();
                }
            }
        }
    }

    class alis implements ActionListener {
        // テキストフィールド・ボタンのアクションリスナ

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bntextf) {
                JTextField tmp = (JTextField) e.getSource();
                sblocks.NumChange(Integer.parseInt(tmp.getText()));
            } else if (e.getSource() == inputtextf) {
                JTextField tmp = (JTextField) e.getSource();
                String temp = tmp.getText();
                String[] temps = temp.split(" ");
                sblocks.setA(temps);
            } else if (e.getSource() == startb) {
                if (!running_state) threadStart();
                else running_state = false;
            } else if (e.getSource() == randomb) {
                sblocks.randomSort();
            } else if (e.getSource() == sort_select) {
                JComboBox<String> tmp = (JComboBox<String>) e.getSource();
                String tstr = (String) tmp.getSelectedItem();
                for (int i = 0; i < SORTING_NUM; i++) {
                    if (tstr == sortstr[i]) {
                        sorting_select = i;
                    }
                }
            } else if (e.getSource() == orderb) {
                reverseAscending_order();
            } else if (e.getSource() == printb) {
                inputtextf.setText(sblocks.getText());
            } else if (e.getSource() == zukei_select) {
                JComboBox<String> tmp = (JComboBox<String>) e.getSource();
                String tstr = (String) tmp.getSelectedItem();
                for (int i = 0; i < ZUKEING_NUM; i++) {
                    if (tstr == zukeistr[i]) {
                        zukeing_select = i;
                    }
                }
            }
            repaint();
        }
    }

    private void threadStart() {
        // ソート開始
        sblocks.ijinit(sorting_select, ascending_order);
        Thread thread = new Thread(this);
        thread.start();
        running_state = true;
        startb.setText("中断");
        System.out.println("THREAD STARTED");
    }

    public void setSortingSelect(int s) {
        // ソートの種類を変える
        sorting_select = s;
        sort_select.setSelectedIndex(sorting_select);
        repaint();
    }

    public void changeDisplay(int s) {
        // 表示方式を変える
        display[s] = !display[s];
        displaybox[s].setSelected(display[s]);
        repaint();
    }

    public void setZukeingSelect(int s) {
        // 図形の形を変える
        zukeing_select = s;
        zukei_select.setSelectedIndex(zukeing_select);
        sblocks.graph_sorting(zukeing_select, bairitu);
        repaint();
    }

    public void setBlockNum(String str) {    // ブロックの個数変換
        sblocks.NumChange(Integer.parseInt(str));
        repaint();
    }

    public void setCode(String str) {    // コード読み込み
        String[] temps = str.split(" ");
        sblocks.setA(temps);
        repaint();
    }

    public void setWaitTime(String str) {    // 待ち時間
        WAIT_TIME = Integer.parseInt(str);
        repaint();
    }

    public void setBairitu(String str) {    // 倍率設定
        bairitu = Integer.parseInt(str);
        repaint();
    }

    public void reverseAscending_order() {    // 昇順
        ascending_order = !ascending_order;
        if (ascending_order) {
            orderb.setText("昇順");
        } else orderb.setText("降順");
    }

}
