package org.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class UI {

    private BufferedImage[] healthBar;
    private BufferedImage healthBarImg;

    public UI() {
        loadAnimation();

    }

    private void loadAnimation() {
        healthBar = new BufferedImage[6];

        try {
            InputStream healthBar_stream = getClass().getResourceAsStream("/UI_HEALTHBAR/05.png");
            assert (healthBar_stream != null);
            healthBarImg = ImageIO.read(healthBar_stream);

            for (int i = 0; i < healthBar.length; i++) {
                healthBar[i] = healthBarImg.getSubimage(i * 48, 16, 48, 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g, int hits) {
        g.drawImage(healthBar[hits], 0, 0, 144, 48, null);
    }

}
