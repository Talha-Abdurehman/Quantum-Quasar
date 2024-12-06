package org.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

public class AudioManager {

    private final Hashtable<String, Clip> SFX;
    private final Clip backgroundSFX;
    private Clip clip;

    public AudioManager() {
        int randNum = randomizer();
        SFX = new Hashtable<>();
        loadAudioClip("Canon_Fire", "/GUNTech_Sci_Fi_Shotgun_Fire_04.wav");
        backgroundSFX = loadBackgroundClip("/Music_Wav_Num/_0" + randNum + ".wav");

    }

    private void loadAudioClip(String name, String path) {
        URL source = getClass().getResource(path);
        assert source != null;
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(source)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            SFX.put(name, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadBackgroundClip(String path) {
        URL source = getClass().getResource(path);
        assert source != null;
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(source)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    public void fireSFX(String name) {
        new Thread(() -> {
            Clip clip = SFX.get(name);

            if (clip.isRunning() && clip.isActive()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }).start();
    }

    public void backgroundSFX() {
        backgroundSFX.loop(Clip.LOOP_CONTINUOUSLY);
        backgroundSFX.start();
    }

    public int randomizer() {
        Random random = new Random();
        return random.nextInt(15) + 1;
    }
}