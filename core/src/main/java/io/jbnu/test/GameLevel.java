package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class GameLevel implements Disposable {
    private float bpm;
    private BeatClock clock;
    private int goalHeight;
    private final Array<Rocket> rockets = new Array<>();
    private Rocket leftRocket, rightRocket;
    private boolean check;
    private Texture texOne, texTwo, texThree;
    private Texture circle;
    private Texture lineTex;
    private BeatIndicator indicator;
    private Sound sound;
    private boolean cleared = false;

    public GameLevel(int level) {
        check = false;
        cleared = false;
        loadTexture();
        sound = Gdx.audio.newSound(Gdx.files.internal("metronome.mp3"));
        configureByLevel(level);
        clock = new BeatClock(bpm);
        indicator = new BeatIndicator(circle, 640f, 420f);
        indicator.setScales(0.7f, 1.35f);
        rocketByLevel(level);
        rockets.add(leftRocket);
        rockets.add(rightRocket);
    }

    private void loadTexture() {
        texOne = new Texture("rocket1.png");
        texTwo = new Texture("rocket2.png");
        texThree = new Texture("rocket3.png");
        circle = new Texture("circle.png");
        lineTex = new Texture("line.png");
    }

    private void configureByLevel(int level) {
        switch (level) {
            case 1:
                bpm = 80f;
                goalHeight = 1000;
                break;
            case 2:
                bpm = 120f;
                goalHeight = 2000;
                break;
            case 3:
                bpm = 120f;
                goalHeight = 2000;
                break;
            case 4:
                bpm = 140f;
                goalHeight = 3000;
                break;
            default:
                bpm = 80f;
                goalHeight = 1000;
                break;
        }
    }

    private void rocketByLevel(int level) {
        float leftX = 400f, rightX = 880f, y = 150f;
        switch (level) {
            case 1:
                leftRocket = new Rocket(RocketType.ONE_BEAT, texOne, leftX, y);
                rightRocket = new Rocket(RocketType.ONE_BEAT, texOne, rightX, y);
                break;
            case 2:
                leftRocket = new Rocket(RocketType.ONE_BEAT, texOne, leftX, y);
                rightRocket = new Rocket(RocketType.ONE_BEAT, texOne, rightX, y);
                break;
            case 3:
                leftRocket = new Rocket(RocketType.ONE_BEAT, texOne, leftX, y);
                rightRocket = new Rocket(RocketType.TWO_BEAT, texTwo, rightX, y);
                break;
            case 4:
                leftRocket = new Rocket(RocketType.ONE_BEAT, texOne, leftX, y);
                rightRocket = new Rocket(RocketType.THREE_BEAT, texThree, rightX, y);
                break;
            default:
                leftRocket = new Rocket(RocketType.ONE_BEAT, texOne, leftX, y);
                rightRocket = new Rocket(RocketType.ONE_BEAT, texOne, rightX, y);
                break;
        }
    }

    public void update(float delta, Ground ground, float gravity) {
        clock.update(delta);
        if (clock.isNewBeat()) {
            sound.play();
            indicator.pulse();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (clock.isInBeatWindow(0.22f) && leftRocket.getGap() == 0) {
                leftRocket.fire();
                System.out.println("GOOD");
            } else {
                leftRocket.startFalling();
                System.out.println("MISS");
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if (clock.isInBeatWindow(0.22f) && rightRocket.getGap() == 0) {
                rightRocket.fire();
                System.out.println("GOOD");
            } else {
                rightRocket.startFalling();
                System.out.println("MISS");
            }
        }
        if (check == false && clock.isInBeatWindow(0.22f)) {
            check = true;
        }
        if (check == true && !clock.isInBeatWindow(0.22f)) {
            check = false;
            for (Rocket r : rockets) {
                if (!r.hasFiredThisBeat()) {
                    if (r.getGap() > 0) {
                        // 쉬는 박자: gap만 줄이고 계속 떠있기
                        r.decreaseGap();
                    } else {
                        // 쉬는 박자 끝난 뒤에도 못 맞추면 이제 낙하
                        r.startFalling();
                    }
                }
                r.resetBeatFlag();
            }
        }
        indicator.update(delta);
        for (Rocket r : rockets) {
            r.update(delta, ground, gravity);
            if (r.getY() >= goalHeight) {
                cleared = true;
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Rocket r : rockets) r.render(batch);
        indicator.render(batch);
        batch.draw(lineTex, 0, goalHeight + 40, 1280, 40);
    }

    public float getHighestRocketY() {
        float max = Float.NEGATIVE_INFINITY;
        for (Rocket r : rockets) {
            if (r.getY() > max) max = r.getY();
        }
        return max;
    }

    public boolean isCleared() {
        return cleared;
    }

    public float getDistanceBetweenRockets() {
        return Math.abs(rockets.get(0).getY() - rockets.get(1).getY());
    }

    @Override
    public void dispose() {
        if (texOne != null) texOne.dispose();
        if (texTwo != null) texTwo.dispose();
        if (texThree != null) texThree.dispose();
        for (Rocket r : rockets) {
            if (r instanceof Disposable) ((Disposable) r).dispose();
        }
    }
}
