package org.example;

public class Game {
    private GameWindow gamewindow;
    private GamePanel gamepanel;

    public Game(){
        gamepanel = new GamePanel();
        gamewindow = new GameWindow(gamepanel);
        gamepanel.requestFocus();
    }
}
