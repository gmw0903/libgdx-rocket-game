package io.jbnu.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * BeatIndicator — pulse()가 호출될 때 잠깐 커졌다 작아지는 간단한 인디케이터
 */
public class BeatIndicator {
    private final Texture circleTex;
    private float cx, cy;

    // 스케일 관련
    private float minScale = 0.7f;
    private float maxScale = 1.35f;
    private float currentScale = 0.7f;
    private float pulseSpeed = 4f;    // 펄스가 줄어드는 속도
    private boolean pulsing = false;

    public BeatIndicator(Texture circleTex, float centerX, float centerY) {
        this.circleTex = circleTex;
        this.cx = centerX;
        this.cy = centerY;
    }

    public void setScales(float min, float max) {
        this.minScale = min;
        this.maxScale = max;
        this.currentScale = min;
    }

    public void setCenter(float x, float y) {
        this.cx = x;
        this.cy = y;
    }

    /**
     * 외부에서 박자마다 호출 (ex: indicator.pulse())
     */
    public void pulse() {
        currentScale = maxScale;
        pulsing = true;
    }

    /**
     * 매 프레임 호출 (감속 효과)
     */
    public void update(float delta) {
        if (pulsing) {
            currentScale -= (maxScale - minScale) * pulseSpeed * delta;
            if (currentScale <= minScale) {
                currentScale = minScale;
                pulsing = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        float w = circleTex.getWidth();
        float h = circleTex.getHeight();
        float drawW = w * currentScale / 4f;
        float drawH = h * currentScale / 4f;
        batch.draw(circleTex, cx - drawW * 0.5f, cy - drawH * 0.5f, drawW, drawH);
    }
}
