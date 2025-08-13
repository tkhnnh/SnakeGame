// THe actual game logic and drawing

import javax.swing.JPanel;
import java.awt.*; // For graphics, Color ,Dimension
import java.util.Random;
import javax.swing.Timer;
import  java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; //Size of snake block
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 100; // Speed of snake (ms per move)

    final int x[] = new int[GAME_UNITS]; // x positions of snake body parts
    final int y[] = new int[GAME_UNITS]; // y positions of snake body parts

    int bodyParts = 6; // starting length of snake
    int applesEaten; // number of apples eaten by snake
    int appleX; //Apple X position
    int appleY; //Apple Y  position
    char direction = 'R'; // initial direction : d=right
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        spawnApple();
        running  = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    //Randomly place the apple
    public void spawnApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            draw(g);
        }else {
            gameOver(g);
        }
    }

    public void draw(Graphics g) {
        //Draw apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        //Draw snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green); //Head
            } else {
                g.setColor(new Color(45,180,0)); // body
            }
            g.fillRect(x[i],y[i], UNIT_SIZE, UNIT_SIZE);
        }

        //Draw Score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        g.drawString("Score: " + applesEaten, 10, 25);

    }

    public  void move(){
        //move body parts
        for (int i = bodyParts; i> 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];

        }

        //move head in current direction
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
        repaint();
    }

    public  void checkCollisions(){
        //check if head collides with body
        for (int i=bodyParts; i > 0; i--){
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
                break;
            }
        }
        //check if head touches left border
        if(x[0] < 0) running = false;
        //check if head touches right border
        if(x[0] >= SCREEN_WIDTH) running =false;
        //check if head touches bottom border
        if(y[0] >= SCREEN_HEIGHT) running = false;
        //check if head touches the top
        if(y[0] < 0) running =false;

        if (!running){
            timer.stop();
        }
    }


    public void gameOver(Graphics g){
        //score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT /2);

        //Final Score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String scoreText = "Score: "+applesEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - metrics1.stringWidth(scoreText)) / 2, SCREEN_HEIGHT /2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    //Keyboard Controls
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
