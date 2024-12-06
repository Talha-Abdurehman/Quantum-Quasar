package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Menu {

    private BufferedImage menuImage;
    private BufferedImage[] menuItems;
    private InputStream[] menuStreams;

    public Menu() throws IOException {
        loadImages();
    }

    public void loadImages() throws IOException {


        menuItems = new BufferedImage[6];

        for (int i = 0; i < 6; i++) {
            try (InputStream stream = getClass().getResourceAsStream(
                    "/Sci_Fi_Game_UI_FREE/PAUSE_MENU/PAUSE-MENU_0000s_000" + i + ".png")) {
                if (stream == null) {
                    throw new IOException("Resource not found: /Sci_Fi_Game_UI_FREE/PAUSE_MENU/PAUSE-MENU_0000s_000" + i + ".png");
                }
                assert menuItems != null;
                menuItems[i] = ImageIO.read(stream);
            }
        }
    }

    public BufferedImage[] getMenuImage() {
        return menuItems;
    }


    public void loadMainMenu() {

    }

    public void loadPauseMenu() {

    }
}
