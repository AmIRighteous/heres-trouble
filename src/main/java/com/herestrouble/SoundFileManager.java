package com.herestrouble;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
public abstract class SoundFileManager {
    public static InputStream getSoundStream(Sound sound) throws FileNotFoundException {
        BufferedInputStream output = (BufferedInputStream) SoundFileManager.class.getResourceAsStream(sound.getResourceName());
        return SoundFileManager.class.getResourceAsStream(sound.getResourceName());
    }
}
