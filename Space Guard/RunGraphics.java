import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.awt.event.MouseEvent;
import java.awt.Image;
import javax.swing.ImageIcon;
public class RunGraphics {

    private JFrame frame;
    int fW = 800;
    int fH = 600;

    // The method to set up
    public RunGraphics() {

        frame = new JFrame("SPACE GAURDS");
        frame.setSize(fW, fH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // the event that triggers the end of the program
        frame.setPreferredSize(frame.getSize());
        frame.add(new showGraphics(frame.getSize())); // Setting up the DrawBars public class function () putting it in this frame)
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    // The main method
    public static void main(String... argv) {
        new RunGraphics();
    }

    public static class showGraphics extends JPanel implements Runnable, MouseListener, KeyListener {

        Image backgroundImage;
        private Thread animator;
        int xAxis = 30;
        int yAxis = 30;
        Ship s;
        Alien[][] a = new Alien[3][10];
        Shot sh;
        boolean gameOn = false;

        public showGraphics(Dimension dimension) {

            s = new Ship(350, 500, 57, 35, 5, "human.png");
            sh = new Shot(400, 500, 50, 20, 15, "shot 2.png");
            int x = 10;
            int y = 10;
            backgroundImage = new ImageIcon("background.jpg").getImage();
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {
                    a[r][c] = new Alien(x, y, 30, 30, 5, "ufo.png");
                    x += 35;
                }
                x = 10;
                y += 25;
            }

            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener(this);
            setFocusable(true);
            if (animator == null) {
                animator = new Thread(this);
                animator.start();
            }
            setDoubleBuffered(true);
        }
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;// g2 is the graphics object that we need to use 
            Dimension d = getSize();// to draw things to the screen
            g2.drawImage(backgroundImage, 0, 0, d.width, d.height, null); // create a background
              if (gameOn == true) {
                moveAlien();
                s.move(0);
                sh.move(0);

            } else {
                g2.setColor(Color.white);
                g2.setFont(new Font("Dialog", Font.BOLD, 30));
                g.drawString("Press S to Start", 250, 250);

            }
            sh.draw(g2);
            s.draw(g2);
            hitDetect();

            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {
                    if (a[r][c].isVis)
                        a[r][c].draw(g2);
                }
            }

        } // end of paintcomponent

        public void hitDetect() {
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {
                    if (a[r][c].isVis == true && sh.getX() + sh.getWidth() >= a[r][c].getX() &&
                            sh.getX() <= a[r][c].getX() + a[r][c].getWidth() &&
                            sh.getY() + sh.getHeight() >= (a[r][c].getY()) &&
                            sh.getY() <= a[r][c].getY() + a[r][c].getHeight()) {

                        a[r][c].isVis = false;
                        sh.x = -30;
                    }
                }
            }

        }

        public void moveAlien() {
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {
                    if (a[r][c].moveLeft)
                        a[r][c].setX(a[r][c].getX() - a[r][c].getSpeed());

                    if (a[r][c].moveRight) {
                        a[r][c].setX(a[r][c].getX() + a[r][c].getSpeed());
                    }
                }
            }
            // check if we need to switch directions
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {

                    if (a[r][c].getX() > 600) {
                        moveLeftRight(1);
                        break;
                    }

                    if (a[r][c].getX() < 0) {
                        moveLeftRight(2);
                        break;
                    }
                }
            }

        }
          public void moveLeftRight(int d) {
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < a[0].length; c++) {
                    if (d == 1) {
                        a[r][c].moveLeft = true;
                        a[r][c].moveRight = false;
                    } else {
                        a[r][c].moveLeft = false;
                        a[r][c].moveRight = true;
                    }

                    a[r][c].setY(a[r][c].getY() + 10);
                    if (a[r][c].getY() > 500) {
                        gameOn = false;
                    }
                }
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
        public void keyTyped(KeyEvent e) { }
        public void keyPressed(KeyEvent e) 
        { 
            int k = e.getKeyCode();
            if (k == 83) {
                gameOn = true;
            }
            s.setLeftRight(k);
            if (k == 32) {
                sh.goUp = true;
                sh.setX(s.getX() + (s.getWidth() / 2));
                sh.setY(s.getY());
            }
        }

        public void keyReleased(KeyEvent e) {
            s.stop();

        }
         public void run() {
            long beforeTime;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 37;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run
    }
}