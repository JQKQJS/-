package com.qjs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//展示主窗口

public class MainFrame extends JFrame {
    private static final String ImagePath = "game\\src\\image\\";
    private static int x0 = 0, y0 = 0;//0.png图片的坐标
    private static boolean start=true;//是否开始
    private static int count=0;//步数
    private int[][] imageData = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };//图片初始位置
    private int [][] winData = {//胜利条件
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    public MainFrame() {
        // 调用一个初始化方法：初始化窗口大小等信息。
        initFrame();
        // 打乱数组色块顺序
        initRandomArray();
        // 初始化界面：展示数字色块。
        initImage();
        // 初始化系统菜单：点击弹出菜单信息是系统退出，重启游戏
        initMenu();
        // 绑定上下左右移动时事件
        initMoveEvent();
        // 设置窗口的显示
        this.setVisible(true);
    }

    private void initMoveEvent() {
        // 上下左右移动,移动的对象是0.png，控制他不能越界
        //移动就是和0.png和周围图片交换，移动的图片是0.png
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!start) {
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        // 用户按了上键，让图片向上移动。
                        switchAndMove(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        // 用户按了下键，让图片向下移动。
                        switchAndMove(Direction.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        // 用户按了左键，让图片向左移动。
                        switchAndMove(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        // 用户按了右键，让图片向右移动。
                        switchAndMove(Direction.RIGHT);
                        break;
                }

                refresh();
                if(iswin()){
                    //创建一个JLabel对象，设置imagepath+win.png给它展示。
                    getContentPane().removeAll();
                    JLabel label = new JLabel(new ImageIcon(ImagePath + "win.png"));
                    label.setBounds(124, 230, 266, 88);
                    add(label);
                    initImage();
                    repaint();//刷新界面
                    start=false;
                }
            }

            private boolean iswin() {
                for (int i = 0; i < imageData.length; i++) {
                    for (int j = 0; j < imageData[i].length; j++) {
                        if (imageData[i][j] != winData[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

    }

    private void refresh() {
        // 移除所有内容面板中的组件（保留菜单条）
        this.getContentPane().removeAll();
        initImage();
        this.repaint();//刷新界面
    }

    private void switchAndMove(Direction direction) {
        switch (direction) {
            case UP:
                if (x0 > 0) {
                    imageData[x0][y0] = imageData[x0 - 1][y0];
                    imageData[x0 - 1][y0] = 0;
                    x0--;count++;
                }
                break;
            case DOWN:
                if (x0 < 3) {
                    imageData[x0][y0] = imageData[x0 + 1][y0];
                    imageData[x0 + 1][y0] = 0;
                    x0++;count++;
                }
                break;
            case LEFT:
                if (y0 > 0) {
                    imageData[x0][y0] = imageData[x0][y0 - 1];
                    imageData[x0][y0 - 1] = 0;
                    y0--;count++;
                }
                break;
            case RIGHT:
                if (y0 < 3) {
                    imageData[x0][y0] = imageData[x0][y0 + 1];
                    imageData[x0][y0 + 1] = 0;
                    y0++;count++;
                }
                break;
            default:
                break;
        }
    }


    private void initRandomArray() {//注意：这里随机打乱数组是有问题的，会有无解问题
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                int x = (int) (Math.random() * 4), y = (int) (Math.random() * 4);
                int temp = imageData[x][y];
                imageData[x][y] = imageData[i][j];
                imageData[i][j] = temp;//随机交换
                if (imageData[i][j] == 0) {
                    x0 = i;
                    y0 = j;//记录0.png图片的坐标
                }
            }
        }
    }
    //创建菜单
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();//创建一个菜单条对象
        JMenu menu = new JMenu("系统");//创建一个菜单对象
        JMenuItem exitJi = new JMenuItem("退出");//创建一个菜单项对象
        JMenuItem restartJi = new JMenuItem("重启");//创建一个菜单项对象
        exitJi.addActionListener(e -> {
            dispose();//销毁！
        });//添加点击事件
        menu.add(exitJi);
        restartJi.addActionListener(e -> {
            //重新启动游戏
            count=0;
            initRandomArray();//重新打乱数组
            refresh();//刷新界面
            start=true;
        });
        menu.add(restartJi);
        menuBar.add(menu);//添加菜单对象到菜单条中
        this.setJMenuBar(menuBar);//添加菜单条到窗口中
    }

    //放入图片
    private void initImage() {
        //展示一个行列矩阵的图片，一次铺满窗口（4*4）
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                //获取图片名称
                int number = imageData[i][j];
                String imageName = number + ".png";

                //获取图片对象
                ImageIcon imageIcon = new ImageIcon(ImagePath + imageName);

                //设置图片位置
                JLabel label = new JLabel(imageIcon);
                label.setBounds(j * 105 + 20, i * 105 + 60, 100, 100);
                this.add(label);
            }
        }
        JLabel label = new JLabel("当前移动"+count+"步");
        label.setBounds(20, 20, 100, 20);
        //把文字展示为红色
        label.setForeground(Color.RED);
        //加粗
        label.setFont(new Font("楷体", Font.BOLD, 12));
        this.add(label);
        //设置背景
        JLabel background = new JLabel(new ImageIcon(ImagePath + "background.png"));
        background.setBounds(0, 0, 450, 484);
        this.add(background);
    }

    //初始化窗口
    private void initFrame() {
        this.setTitle("华容道");//设置窗口标题
        this.setSize(465, 575);//设置窗口大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭方式
        this.setLocationRelativeTo(null);//设置窗口居中
        this.setLayout(null);//设置布局为绝对定位
        //绝对定位的作用：窗口中的组件，可以任意的设置组件的坐标和宽高。
    }
}
