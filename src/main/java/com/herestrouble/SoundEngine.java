package com.herestrouble;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private HeresTroubleConfig config;

    private static final long CLIP_MTIME_UNLOADED = -2;
    private long lastClipMTime = CLIP_MTIME_UNLOADED;
    private Clip clip = null;

    private boolean loadClip(Sound sound) {
        try (InputStream stream = new BufferedInputStream(SoundFileManager.getSoundStream(sound))) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
                clip.open(audioInputStream);
            }
            return true;
        } catch ( UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.warn("Failed to load sound: " + sound, e);
        }
        return false;
    }

    public void playClip(Sound sound) {
        long currentMTime = System.currentTimeMillis();
        if (clip == null || currentMTime != lastClipMTime || !clip.isOpen()) {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                lastClipMTime = CLIP_MTIME_UNLOADED;
                log.warn("Failed to get clip for here's trouble sound " + sound, e);
                return;
            }
            lastClipMTime = currentMTime;
            if (!loadClip(sound)) {
                return;
            }
        }

        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain  = 20f * (float) Math.log10(config.pluginVolume()/100f);
        gain = Math.min(gain, volume.getMaximum());
        gain = Math.max(gain, volume.getMinimum());
        volume.setValue(gain);
        clip.loop(0);
    }
}
