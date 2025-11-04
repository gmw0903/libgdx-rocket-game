package io.jbnu.test;

public enum RocketType {
    ONE_BEAT("rocket1.png", 1f),
    TWO_BEAT("rocket2.png", 2f),
    THREE_BEAT("rocket3.png", 3f);

    public final String texturePath;
    public final float beatInterval;

    RocketType(String texture, float interval) {
        this.texturePath = texture;
        this.beatInterval = interval;
    }
}
