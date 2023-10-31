package com.herestrouble;

public enum Sound {
    HERES_TROUBLE("/heres_trouble.wav");

    private final String resourceName;

    Sound(String name) {
        resourceName = name;
    }
    String getResourceName() {
        return resourceName;
    }

}
