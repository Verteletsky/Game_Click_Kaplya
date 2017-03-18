package ru.now_android.catch_and_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow gameWindow;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image drop;
    private static float dropLeftX = 200;
    private static float dropTopY = -100;
    private static float dropSpeedV = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(200, 100);
        gameWindow.setSize(906, 478);
        gameWindow.setResizable(false);
        lastFrameTime =  System.nanoTime();
        final GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeftX + drop.getWidth(null);
                float dropBot = dropTopY + drop.getHeight(null);
                boolean isDrop = x >= dropLeftX && x <= dropRight && y >= dropTopY && y <= dropBot;
                if (isDrop) {
                    dropTopY = -100;
                    dropLeftX = (int) (Math.random() * (gameField.getWidth() - drop.getWidth(null)));
                    dropSpeedV = dropSpeedV + 20;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }
            }
        });
        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;
        dropTopY = dropTopY + dropSpeedV * deltaTime;
        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int) dropLeftX, (int) dropTopY, null);
        if (dropTopY > gameWindow.getHeight()) g.drawImage(gameOver, 280, 120, null);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
