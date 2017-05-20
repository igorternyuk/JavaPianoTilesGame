package tedonttouchthewhitetile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class View implements ViewUpdater {
    private final String TITLE_OF_PROGRAM = "TeDon'tTouchTheWhiteTile";
    private final int WINDOW_WIDTH = 450, WINDOW_HEIGHT = 628;
    private final ConstantModel model;
    private final  Controller controller;
    private final JFrame window;
    private final JLabel lblInfo;
    private final JLabel lblScore;
    private final Canvas canvas;
    private final Font font = new Font("Arial", Font.BOLD, 40);
    public View(ConstantModel model, Controller controller){
        this.model = model;
        this.controller = controller;
        window = new JFrame(TITLE_OF_PROGRAM);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setFocusable(true);
        window.requestFocus();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_N:
                        controller.newGame();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                    case KeyEvent.VK_P:
                        controller.printInfo();
                        break;
                    default:
                        break;
                }
            }
        });
        canvas = new Canvas();
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                int cursorX = e.getX(), cursorY = e.getY();
                controller.click(cursorX, cursorY);
            }
        });
        lblInfo = new JLabel();
        lblInfo.setForeground(Color.red);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 70));
        lblScore = new JLabel();
        lblScore.setForeground(Color.MAGENTA.darker());
        lblScore.setFont(new Font("Arial", Font.BOLD, 40));
        canvas.add(lblInfo);
        canvas.add(lblScore);
        window.getContentPane().add(BorderLayout.CENTER, canvas);
        window.add(canvas);

        window.setVisible(true);
    }
    
    public void draw(Graphics2D g){
        drawTiles(g);
        lblScore.setText("SCORE: " + model.getScore());
        lblScore.setLocation((WINDOW_WIDTH - lblScore.getBounds().width) / 2, 50);
        if(model.isGameOver()){
            lblInfo.setText("GAME OVER");
            lblInfo.setLocation((WINDOW_WIDTH - lblInfo.getBounds().width) / 2,
                                (WINDOW_HEIGHT - lblInfo.getBounds().height) / 2);
        } else {
            lblInfo.setText("");
        }
    }

    @Override
    public void updateView() {
        canvas.repaint();
    }
    private class Canvas extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            draw((Graphics2D)g);
        }
    }
    
    private void drawTiles(Graphics2D g) {
        for (int i = 0; i < model.getTileNumber(); ++i) {
            int x = model.getTileX(i);
            int y = model.getTileY(i);
            boolean isBlack = model.isTileBlack(i);
            g.setColor(model.isTileBlack(i) ? Color.BLACK : Color.WHITE);
            g.fillRect(model.getTileX(i), model.getTileY(i),
                       Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
            g.setColor(model.isTileBlack(i) ? Color.WHITE : Color.BLACK);
            g.drawRect(model.getTileX(i), model.getTileY(i),
                       Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        }
    }    
}
