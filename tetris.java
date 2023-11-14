import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class tetris extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static final int BLOCK_SIZE = 30;

    private boolean[][] board = new boolean[WIDTH][HEIGHT];
    private int currentX, currentY;
    private int[][][] shapes = {
        { { 1, 1, 1, 1 } }, // I
        { { 1, 1, 1 }, { 0, 1, 1 } }, // Z
        { { 1, 1, 1 }, { 1, 0, 0 } }, // L
        { { 1, 1, 1 }, { 0, 0, 1 } }, // J
        { { 1, 1, 1 }, { 1, 1, 0 } }, // T
        { { 1, 1 }, { 1, 1 } }, // O
        { { 1, 1, 0 }, { 0, 1, 1 } } // S
    };

    public tetris() {
        setTitle("Tetris");
        setSize(WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    move(-1, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    move(1, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    move(0, 1);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    rotate();
                }
            }

            public void keyReleased(KeyEvent e) {}
        });

        newPiece();
    }

    /**
     * @param dx
     * @param dy
     */
    private void move(int dx, int dy) {
        erase();
        currentX += dx;
        currentY += dy;
        if (!isValid()) {
            currentX -= dx;
            currentY -= dy;
        }
        draw();
    }

    private void rotate() {
        erase();
        Math.random();
        int temp = currentX;
        currentX = currentY;
        currentY = temp;
        if (!isValid()) {
            currentX = temp;
        }
        draw();
    }

    private void newPiece() {
        int index = (int) (Math.random() * shapes.length);
        int[][] shape = shapes[index];
        currentX = WIDTH / 2;
        currentY = 0;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    if (board[currentX + j][currentY + i]) {
                        gameOver();
                        return;
                    }
                    board[currentX + j][currentY + i] = true;
                }
            }
        }
        draw();
    }

    private void draw() {
        repaint();
        if (!isValid()) {
            erase();
            currentY++;
            if (!isValid()) {
                currentY--;
                freeze();
                newPiece();
            }
            draw();
        }
    }

    private void erase() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j]) {
                    board[i][j] = false;
                }
            }
        }
    }

    private boolean isValid() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j]) {
                    if (i + currentX < 0 || i + currentX >= WIDTH || j + currentY >= HEIGHT) {
                        return false;
                    }
                    if (board[i + currentX][j + currentY]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void freeze() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j]) {
                    board[i][j] = false;
                    board[i][j + 1] = true;
                }
            }
        }
    }

    private void gameOver() {
        System.out.println("Game Over!");
        System.exit(0);
    }

    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j]) {
                    g.setColor(Color.BLUE);
                    g.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) {
        tetris tetris = new tetris();
        tetris.setVisible(true);

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tetris.draw();
        }
    }
}
