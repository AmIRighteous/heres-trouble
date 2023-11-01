package com.herestrouble;

public enum Sound {
    HERES_TROUBLE_1("/heres_trouble1.wav"),
    HERES_TROUBLE_2("/heres_trouble2.wav"),
    HERES_TROUBLE_3("/heres_trouble3.wav");

    private final String resourceName;

    Sound(String name) {
        resourceName = name;
    }
    String getResourceName() {
        return resourceName;
    }

    public static Sound getSound(int id) {
        switch (id) {
            case 1:
                return Sound.HERES_TROUBLE_1;
            case 2:
                return Sound.HERES_TROUBLE_2;
            default:
                return Sound.HERES_TROUBLE_3;
        }


    }

}
