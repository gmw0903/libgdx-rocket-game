package io.jbnu.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Rocket {
    private final RocketType type;
    private final Texture texture;
    private float x, y;
    private float vy = 0;
    private float targetY;
    private static final float W = 128f, H = 128f;
    private int gap = 0;

    private boolean rising = false;
    private boolean falling = false;
    private boolean firedThisBeat = false;

    // 타입별 상승 높이
    private static final float HEIGHT_TYPE1 = 80f;
    private static final float HEIGHT_TYPE2 = 160f;
    private static final float HEIGHT_TYPE3 = 240f;

    public Rocket(RocketType type, Texture texture, float startX, float startY) {
        this.type = type;
        this.texture = texture;
        this.x = startX;
        this.y = startY;
        this.targetY = startY;
    }

    public void fire() {
        if (gap == 0 && !rising) {
            firedThisBeat = true;
            rising = true;
            falling = false;
            switch (type) {
                case ONE_BEAT:
                    targetY = y + HEIGHT_TYPE1;
                    break;
                case TWO_BEAT:
                    targetY = y + HEIGHT_TYPE2;
                    gap = 1;
                    break;
                case THREE_BEAT:
                    targetY = y + HEIGHT_TYPE3;
                    gap = 2;
                    break;
                default:
                    targetY = y + HEIGHT_TYPE1;
                    break;
            }
        }
    }

    public void startFalling() {
        if (!rising) falling = true;
    }

    public void update(float delta, Ground ground, float gravity) {
        float floor = ground.getBounds().y + ground.getBounds().height;

        // 상승 중
        if (rising) {
            float speed = 400f * delta;
            y += speed;
            if (y >= targetY) {
                y = targetY;
                rising = false;  // 상승 종료, 그 자리에서 정지
            }
        }

        // 낙하 중
        if (falling) {
            vy += gravity * delta;
            y += vy * delta;

            if (y <= floor) {
                y = floor;
                vy = 0f;
                falling = false; // 바닥 도착 시 멈춤
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - W / 2f, y - H / 2f, W, H);
    }

    // === 유틸 ===
    public void decreaseGap() {
        if (gap > 0) gap--;
    }

    public boolean hasFiredThisBeat() {
        return firedThisBeat;
    }

    public void resetBeatFlag() {
        firedThisBeat = false;
    }

    public int getGap() {
        return gap;
    }

    public float getY() {
        return y;
    }
}
