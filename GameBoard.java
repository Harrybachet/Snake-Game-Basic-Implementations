import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int ALL_TILES = (BOARD_WIDTH * BOARD_HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int DELAY = 150;

    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];
    private int snakeLength;

    private int foodX;
    private int foodY;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(new TAdapter());
        initGame();
    }

    private void initGame() {
        snakeLength = 3;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 50 - i * TILE_SIZE;
            y[i] = 50;
        }

        locateFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);

            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - fm.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
    }

    private void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            snakeLength++;
            locateFood();
        }
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= TILE_SIZE;
        }

        if (rightDirection) {
            x[0] += TILE_SIZE;
        }

        if (upDirection) {
            y[0] -= TILE_SIZE;
        }

        if (downDirection) {
            y[0] += TILE_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (x[0] < 0 || x[0] >= BOARD_WIDTH || y[0] < 0 || y[0] >= BOARD_HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateFood() {
        int randomPos = (int) (Math.random() * (BOARD_WIDTH / TILE_SIZE));
        foodX = randomPos * TILE_SIZE;

        randomPos = (int) (Math.random() * (BOARD_HEIGHT / TILE_SIZE));
        foodY = randomPos * TILE_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFood();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
