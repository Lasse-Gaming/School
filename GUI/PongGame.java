import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
    private int ballX = 250, ballY = 250, ballXDir = -1, ballYDir = -2;
    private int paddle1Y = 100, paddle2Y = 100;
    private int PADDLE_WIDTH_1 = 10, PADDLE_WIDTH_2 = 10, PADDLE_HEIGHT_1 = 60, PADDLE_HEIGHT_2 = 60;
    private final int BALL_SIZE = 20;
    private Timer timer;
    
    // Powerup-Position und Status
    private int powerupX = -50, powerupY = -50;
    private boolean powerUpSpawned = false;
    private int powerUpCount = 0;

    public PongGame() {
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(5, this);
        timer.start();
    }

    public void resetStats(){
        PADDLE_WIDTH_1 = 10; PADDLE_WIDTH_2 = 10; PADDLE_HEIGHT_1 = 60; PADDLE_HEIGHT_2 = 60;
        ballXDir = -1; ballYDir = -2;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 500);

        g.setColor(Color.WHITE);
        g.fillRect(10, paddle1Y, PADDLE_WIDTH_1, PADDLE_HEIGHT_1);
        g.fillRect(480, paddle2Y, PADDLE_WIDTH_2, PADDLE_HEIGHT_2);

        g.setColor(Color.YELLOW);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Zeichne Powerup, wenn es aktiv ist
        if (powerUpSpawned) {
            g.setColor(Color.GREEN); // Powerup-Farbe
            g.fillRect(powerupX, powerupY, 20, 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        if(powerUpCount % 800 == 0){            // wenn 800 * 5 ticks vergangen sind führe aus
            powerUpCount +=1;
            if (!powerUpSpawned){
            spawnPowerup();}
            
        }else{
            powerUpCount +=1;
        }
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballY <= 0 || ballY >= 480) {
            ballYDir = -ballYDir;
        }
        if (ballX <= 10 + PADDLE_WIDTH_1 && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT_1) {
            ballXDir = -ballXDir;
        }
        if (ballX >= 470 - PADDLE_WIDTH_2 && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT_2) {
            ballXDir = -ballXDir;
        }

        if (ballX < 0 || ballX > 500) {
            ballX = 250;
            ballY = 250;
            resetStats();  // reset stats
        }
        

        repaint();
    }

    // Powerup zufällig spawnen
    private void spawnPowerup() {

        powerUpSpawned = true;
        Random rand = new Random();
        powerupX = rand.nextInt(460) + 20;  // x-Position zwischen 20 und 480
        powerupY = rand.nextInt(440) + 20;  // y-Position zwischen 20 und 440
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && paddle1Y > 0) {
            paddle1Y -= 15;
        }
        if (e.getKeyCode() == KeyEvent.VK_S && paddle1Y < 440) {
            paddle1Y += 15;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && paddle2Y > 0) {
            paddle2Y -= 15;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && paddle2Y < 440) {
            paddle2Y += 15;
        }

        // Powerup für Spieler 1 (K drücken)
        if (e.getKeyCode() == KeyEvent.VK_K && powerUpSpawned) {
            Random random = new Random();
            int powerUp = random.nextInt(1);
            switch (1) {
                case 1:
                    PADDLE_HEIGHT_1 += 30;
                    PADDLE_WIDTH_1 += 30;
                    powerUpSpawned = false;
                    break;
                
                default:
                    break;
            }
            
        }

        // Powerup für Spieler 2 (L drücken)
        if (e.getKeyCode() == KeyEvent.VK_L && powerUpSpawned) {
            Random random = new Random();
            int powerUp = random.nextInt(1);
            switch (1) {
                case 1:
                    PADDLE_HEIGHT_2 += 30;
                    PADDLE_WIDTH_2 += 30;
                    powerUpSpawned = false;
                    break;
                
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Game");
        PongGame pongGame = new PongGame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pongGame);
        frame.setVisible(true);
    }
}