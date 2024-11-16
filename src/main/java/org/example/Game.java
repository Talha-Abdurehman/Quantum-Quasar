package org.example;

public class Game {

    public Game(){
        GamePanel gamepanel = new GamePanel();
        GameWindow gamewindow = new GameWindow(gamepanel);
        gamepanel.requestFocus();
    }
}
