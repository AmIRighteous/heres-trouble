package com.herestrouble;

import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opencl.CL;

import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Singleton
@Slf4j
public class SoundEngine {
    private static final long CLIP_MTIME_UNLOADED = -2;
    private long lastClipMTime = CLIP_MTIME_UNLOADED;
    private Clip clip = null;

    private boolean loadClip(Sound sound) throws FileNotFoundException {
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

    public long playClip(Sound sound) throws FileNotFoundException {
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
                return -1;
            }
            lastClipMTime = currentMTime;
            if (!loadClip(sound)) {
                return -1;
            }
        }

        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain  = 20f * (float) Math.log10(10);
        gain = Math.min(gain, volume.getMaximum());
        gain = Math.max(gain, volume.getMinimum());
        volume.setValue(gain);
        clip.loop(0);

        return clip.getMicrosecondLength();
    }
}
