package com.herestrouble;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
public abstract class SoundFileManager {
    public static InputStream getSoundStream(Sound sound) throws FileNotFoundException {
        log.info("about to get sound " + sound.getResourceName());
        BufferedInputStream output = (BufferedInputStream) SoundFileManager.class.getResourceAsStream(sound.getResourceName());
        log.info("output = " + output);
        return SoundFileManager.class.getResourceAsStream(sound.getResourceName());
    }
}
