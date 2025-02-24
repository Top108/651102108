import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BallGame extends JPanel implements ActionListener, KeyListener {
    private int ballX, ballY, ballSize = 20;
    private int redBallSize = 15;
    private int paddleX, paddleY, paddleWidth = 40, paddleHeight = 10;
    private int ballSpeedY = 3;
    private int score = 0;
    private static int highScore = 0;
    private Timer timer;
    private Random random;
    private int redBallCount = 5;
    private int[] redBallX, redBallY;
    private int redBallSpeedY = 3;
    private boolean gameOver = false;

    // ตัวแปรสำหรับการควบคุมการกดปุ่ม
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int paddleSpeed = 5; // ความเร็วในการเคลื่อนที่ของ paddle

    public BallGame() {
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        random = new Random();
        initGame();
        
        timer = new Timer(5, this);
        timer.start();
    }
    
    private void initGame() {
        // ใช้ขนาดที่แน่นอน (500) จากการกำหนด PreferredSize
        ballX = random.nextInt(500 - ballSize);
        ballY = 0;
        paddleX = 100;
        paddleY = 490;
        score = 0;
        gameOver = false;
        
        redBallX = new int[redBallCount];
        redBallY = new int[redBallCount];
        for (int i = 0; i < redBallCount; i++) {
            redBallX[i] = random.nextInt(500 - redBallSize);
            redBallY[i] = random.nextInt(500 - redBallSize);  // สามารถใช้ 500 แทน getWidth()
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High Score: " + highScore, 10, 40);
        
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, ballSize, ballSize);
        
        g.setColor(Color.RED);
        for (int i = 0; i < redBallCount; i++) {
            g.fillOval(redBallX[i], redBallY[i], redBallSize, redBallSize);
        }
        
        g.setColor(Color.WHITE);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
        
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", getWidth() / 2 - 80, getHeight() / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press SPACE to restart", getWidth() / 2 - 110, getHeight() / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            return;
        }
        
        // อัปเดตตำแหน่ง paddle จากการกดปุ่ม
        if (leftPressed && paddleX > 0) {
            paddleX -= paddleSpeed;
        }
        if (rightPressed && paddleX < getWidth() - paddleWidth) {
            paddleX += paddleSpeed;
        }
        
        ballY += ballSpeedY;
        for (int i = 0; i < redBallCount; i++) {
            redBallY[i] += redBallSpeedY;
        }
        
        // ตรวจจับการชนของลูกบอลหลักกับ paddle
        if (ballY >= paddleY - ballSize && ballX + ballSize >= paddleX && ballX <= paddleX + paddleWidth) {
            score++;
            if (score > highScore) {
                highScore = score;
            }
            ballY = 0;
            ballX = random.nextInt(500 - ballSize);
        }
        
        for (int i = 0; i < redBallCount; i++) {
            if (redBallY[i] >= paddleY - redBallSize && redBallX[i] + redBallSize >= paddleX && redBallX[i] <= paddleX + paddleWidth) {
                gameOver = true;
                timer.stop();
            }
            if (redBallY[i] > getHeight()) {
                redBallY[i] = 0;
                redBallX[i] = random.nextInt(500 - redBallSize);
            }
        }
        
        if (ballY > getHeight()) {
            ballY = 0;
            ballX = random.nextInt(500 - ballSize);
        }
        
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                initGame();
                timer.start();
                repaint();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ball Game");
        BallGame game = new BallGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
