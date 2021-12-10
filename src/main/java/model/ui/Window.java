package model.ui;

import model.Coord;
import model.Mapable;
import service.FlyFigure;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements Runnable, Mapable {

    private Box[][] boxes;
    private FlyFigure fly;

    public Window () {
        boxes = new Box[Config.WIDTH][Config.HEIGHT];
        initForm();
        initBoxes();
        addKeyListener(new KeyAdapter());
        TimerAdapter timeAdapter = new TimerAdapter();
        Timer timer = new Timer(300, timeAdapter);
        timer.start();
    }

    public void run() {

       repaint();
//        Window window = new Window();
    }

    public void addFigure() {
        fly = new FlyFigure(this);
        if (fly.canPlaceFigure()) {
            showFigure();
        } else {
            setVisible(false);
            dispose();
        }
    }

    private void initForm () {
        setSize(Config.WIDTH * Config.SIZE + 15,
                Config.HEIGHT * Config.SIZE + 40);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Tetris the Game");
        setLayout(null);
        setVisible(true);
    }

    private void initBoxes () {
        for (int x = 0; x < Config.WIDTH; x++) {
            for (int y = 0; y < Config.HEIGHT; y++){
                boxes[x][y] = new Box(x, y);
                add(boxes[x][y]);
            }
        }
    }

    private void showFigure(){
        showFigure(1);
    }

    private void hideFigure(){
        showFigure(0);
    }

    private void showFigure (int color) {
        for (Coord dot : fly.getFigure().dots) {
            setBoxColor(fly.getCoord().x + dot.x, fly.getCoord().y + dot.y, color);
        }
    }

    private void setBoxColor(int x, int y, int color) {
        if (x < 0 || x >= Config.WIDTH) return;
        if (y < 0 || y >= Config.HEIGHT) return;
        boxes[x][y].setColor(color);
    }

    public int getBoxColor(int x, int y){
        if (x < 0 || x >= Config.WIDTH) return -1;
        if (y < 0 || y >= Config.HEIGHT) return -1;
        return boxes[x][y].getColor();
    }

    private void moveFly(int sx, int sy) {
        hideFigure();
        fly.moveFigure(sx,sy);
        showFigure();
    }

    private void turnFly(int direction) {
        hideFigure();
        fly.turnFigure(direction);
        showFigure();
    }

    class KeyAdapter implements KeyListener {
        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT : moveFly(-1,0); break;
                case KeyEvent.VK_RIGHT : moveFly(+1,0); break;
                case KeyEvent.VK_UP : turnFly(1); break;
                case KeyEvent.VK_DOWN : turnFly(2); break;
                case KeyEvent.VK_U : moveFly(0,-1); break;
            }
        }
        public void keyReleased(KeyEvent e) {}
    }
    void stripLines() {
        for (int y = Config.HEIGHT - 1; y >= 0 ; y--) {
            while (isFullLine(y)) {
                dropLine(y);
            }
        }
    }

    private void dropLine (int y) {
        for (int my = y - 1; my >= 0 ; my--) {
            for (int x = 0; x < Config.WIDTH; x++)
                setBoxColor(x, my + 1, getBoxColor(x, my));
        }
        for (int x = 0; x < Config.WIDTH; x++)
            setBoxColor(x, 0, 0);
    }

    private boolean isFullLine(int y) {
        for (int x = 0; x < Config.WIDTH ; x++) {
            if(getBoxColor(x,y) != 2)
                return false;
        }
        return true;
    }

    class TimerAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            moveFly(0,1);
            if (fly.isLanded()) {
                showFigure(2);
                stripLines ();
                addFigure();
            }
        }
    }
}
