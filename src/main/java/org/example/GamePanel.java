package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;

import static org.constants.Constants.PlayerConstants.*;
import static org.example.Game.GAME_HEIGHT;
import static org.example.Game.GAME_WIDTH;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;


public class GamePanel extends JPanel {
    MouseInput mouseinput = new MouseInput(this);
    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);

    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
