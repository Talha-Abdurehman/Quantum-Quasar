package org.audio;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AudioLoader {

    Clip BACKGROUND;
    AudioInputStream audioInputStream;
    String path;
    Clip BASE_CANON_FIRE;

    public AudioLoader() throws UnsupportedAudioFileException,
            IOException,
            LineUnavailableException {

        URL BASE_CANON_FIRE_source = getClass().getResource("/GUNTech_Sci_Fi_Shotgun_Fire_04.wav");
        assert BASE_CANON_FIRE_source != null;
        audioInputStream = AudioSystem.getAudioInputStream(BASE_CANON_FIRE_source);
        BASE_CANON_FIRE = AudioSystem.getClip();
        BASE_CANON_FIRE.open(audioInputStream);

        URL BACKGROUND_source = getClass().getResource("/Interstellar.wav");
        assert BACKGROUND_source != null;
        audioInputStream = AudioSystem.getAudioInputStream(BACKGROUND_source);
        BACKGROUND = AudioSystem.getClip();
        BACKGROUND.open(audioInputStream);


    }


    public void playSound() {

        new Thread(() -> {
            if (BASE_CANON_FIRE.isRunning() || BASE_CANON_FIRE.isActive()) {
                BASE_CANON_FIRE.stop();
            }
            BASE_CANON_FIRE.setFramePosition(0);
            BASE_CANON_FIRE.start();
        }).start();
    }

    public void playBackground() {
        BACKGROUND.loop(Clip.LOOP_CONTINUOUSLY);
        BACKGROUND.start();
    }

}
