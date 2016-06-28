import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends JFrame {
    private int dimX = 10;
    private int dimY = 20;
    private int prevX = 0;
    private int initX = dimX / 2 - 1;
    private int initY = -1;
    private int CellSize = 30;
    private boolean startGame = false;
    private boolean bottomEdge = false;
    private boolean gameOver = false;
    private int selectedFigure = 0;
    private int nextFigure = 0;
    private int[][] arr;
    private int[][] nextArr;
    private int[][] cashArr;
    private boolean moveX = false;
    private Figures newFigure = null;
    private Figures[] figures = new Figures[7];
    private int[][] cashField = new int[dimX][dimY];
    private int[][] gameField = new int[dimX][dimY];

    private Tetris() {
        super("TETRIS");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(CellSize*18, CellSize*23);
        setLocationRelativeTo(null);
        initComponents();
        setLayout(null);
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
        setVisible(true);
    }
    private void initComponents(){
        GamePanel gp = new GamePanel();
        gp.setBounds(10, 10, dimX * CellSize + 1, dimY * CellSize + 1);
        this.add(gp);
        nextFigurePanel nfp = new nextFigurePanel();
        nfp.add(new JLabel("Next figure"));
        //nfp.setBorder(new LineBorder(Color.GRAY, 2));
        nfp.setBounds(dimX * CellSize + 30, 10, 160, 120);
        this.add(nfp);
        JButton startB = new JButton("START");
        startB.addActionListener(new StartGame());
        startB.setBounds(dimX * CellSize + 60, 200, 80, CellSize);
        this.add(startB);
    }
    private void removeX(int jj) {
        for (int i = 0; i < dimX; i++) {
            cashField[i][jj] = 0;
        }
        for (int j = jj; j > 1; j--) {
            for (int i = 0; i < dimX; i++) {
                cashField[i][j] = cashField[i][j - 1];
            }
        }
        repaint();
    }

    private boolean collision(int iX, int iY) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                try {
                    if (arr[i][j] == 1) {
                        if (cashField[i + iX][j + iY] > 0)
                            return true;
                    }
                } catch (ArrayIndexOutOfBoundsException ae) {
                    return true;
                }
            }
        }
        return false;
    }

    private void dropping() {
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                if (gameField[i][j] > 0)
                    cashField[i][j] += gameField[i][j] + selectedFigure;
            }
        }
    }

    private void addFigure(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                gameField[i + initX][j] = arr[i][j];
                if (cashField[i + initX][j] > 0 && arr[i][j] == 1) {
                    gameOver = true;
                }
            }
        }
    }

    private void checkField() {
        boolean check = false;
        for (int j = 0; j < dimY; j++) {
            for (int i = 0; i < dimX; i++) {
                if (cashField[i][j] == 0)
                    check = true;
            }
            if (!check)
                removeX(j);
            check = false;
        }
    }

    private void clearGameField() {
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                gameField[i][j] = 0;
            }
        }
    }
    private void checkBottomEdgeCollision(){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] == 1) {
                    gameField[i + initX][j + initY] = arr[i][j];
                    if (gameField[i + initX][dimY - 1] == 1)
                        bottomEdge = true;
                }
            }
        }
    }
    private void moveF() {
        if (!bottomEdge && !collision(initX, initY)) {
            clearGameField();
            checkBottomEdgeCollision();
        } else {
            dropping();
            initY = -1;
            initX = dimX / 2 - 1;
            bottomEdge = false;
            selectedFigure = nextFigure;
            nextFigure = (int) Math.round(Math.random() * 6);
            newFigure.setArray(cashArr);
            newFigure = figures[selectedFigure];
            arr = newFigure.getArray();
            nextArr = figures[nextFigure].getArray();
            cashArr = newFigure.getArray();
            addFigure(arr);
            checkField();
            clearGameField();
        }
    }

    private void moveFX() {
        if (collision(initX, initY) && moveX)
            initX = prevX;
        if (!collision(initX, initY)) {
            clearGameField();
            moveX = false;
            checkBottomEdgeCollision();
        }
    }


    private class MyDispatcher implements KeyEventDispatcher {
        private int[][] prevArr;

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                // rotation
                if (e.getKeyCode() == 38) {
                    newFigure.rotate();
                    prevArr = arr;
                    arr = newFigure.getArray();
                    if (collision(initX, initY)) {
                        arr = prevArr;
                        newFigure.setArray(prevArr);
                    }
                    moveFX();
                    repaint();
                }
                // left motion
                if (e.getKeyCode() == 37) {
                    prevX = initX;
                    initX--;
                    moveX = true;
                    moveFX();
                    repaint();
                    // right motion
                } else if (e.getKeyCode() == 39) {
                    prevX = initX;
                    moveX = true;
                    initX++;
                    moveFX();
                    repaint();
                    // down motion
                } else if (e.getKeyCode() == 40) {
                    initY++;
                    moveF();
                    repaint();
                }
            }
            return false;
        }
    }

    private class nextFigurePanel extends JPanel {
        public void paintComponent(Graphics gr) {
            if (startGame) {
                for (int i = 0; i < nextArr.length; i++) {
                    for (int j = 0; j < nextArr.length; j++) {
                        switchColorOfFigure(nextFigure, gr);
                        if (nextArr[i][j] == 1) {
                            gr.fillRect(i * 20 + 50, j * 20 + 30, 20, 20);
                            gr.setColor(Color.BLACK);
                            gr.drawRect(i * 20 + 50, j * 20 + 30, 20, 20);
                        }
                    }
                }
            }
        }
    }

    private void switchColorOfFigure(int nextF, Graphics g) {
        switch (nextF) {
            case 0:
                g.setColor(Color.GREEN);
                break;
            case 1:
                g.setColor(Color.RED);
                break;
            case 2:
                g.setColor(Color.YELLOW);
                break;
            case 3:
                g.setColor(Color.PINK);
                break;
            case 4:
                g.setColor(Color.BLUE);
                break;
            case 5:
                g.setColor(Color.MAGENTA);
                break;
            case 6:
                g.setColor(Color.CYAN);
                break;
            case 7:
                g.setColor(Color.BLACK);
                break;
            default:
                g.setColor(Color.ORANGE);
                break;
        }
    }

    private class GamePanel extends JPanel {
        public void paintComponent(Graphics gr) {

//заполнение игрового поля серым
            gr.setColor(Color.GRAY);
            gr.fillRect(0, 0, this.getWidth(), this.getHeight());

//перерисовка движущейся фигуры
            for (int i = 0; i < dimX; i++) {
                for (int j = 0; j < dimY; j++) {
                    switchColorOfFigure(selectedFigure, gr);

                    if (gameField[i][j] == 1) {
                        gr.fillRect(i * CellSize, j * CellSize, CellSize, CellSize);
                        gr.setColor(Color.BLACK);
                        gr.drawRect(i * CellSize, j * CellSize, CellSize, CellSize);
                    }
// перерисовка застывших вигур
                    if (cashField[i][j] > 0) {
                        switchColorOfFigure(cashField[i][j]-1,gr);
                        gr.fillRect(i * CellSize, j * CellSize, CellSize, CellSize);
                        gr.setColor(Color.BLACK);
                        gr.drawRect(i * CellSize, j * CellSize, CellSize, CellSize);
                    }
                }
            }
        }
    }

    private class StartGame implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            Thread gameThread = new Thread() {
                public void run() {
                    figures[0] = new Figures3("gFigure1", new int[][]{{0, 1, 0}, {0, 1, 0}, {0, 1, 1}});
                    figures[1] = new Figures3("gFigure2", new int[][]{{0, 1, 0}, {0, 1, 0}, {1, 1, 0}});
                    figures[2] = new Figures3("tFigure", new int[][]{{0, 0, 0}, {1, 1, 1}, {0, 1, 0}});
                    figures[3] = new Figures1s("sFigure1", new int[][]{{1, 0, 0}, {1, 1, 0}, {0, 1, 0}});
                    figures[4] = new Figures2s("sFigure2", new int[][]{{0, 1, 0}, {1, 1, 0}, {1, 0, 0}});
                    figures[5] = new FiguresS("square", new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 0}});
                    figures[6] = new FiguresL("line",
                            new int[][]{{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}});
                    selectedFigure = (int) Math.round(Math.random() * 6);
                    newFigure = figures[selectedFigure];
                    arr = newFigure.getArray();
                    cashArr = newFigure.getArray();
                    nextFigure = (int) Math.round(Math.random() * 6);
                    nextArr = figures[nextFigure].getArray();
                    startGame = true;
                    addFigure(arr);
                    while (!gameOver) {
                        initY++;
                        moveF();
                        repaint();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            gameThread.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Tetris();
            }
        });
    }
}
