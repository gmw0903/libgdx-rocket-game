package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameWorld implements Disposable {
    public final float WORLD_GRAVITY = -9.8f * 200; // 초당 중력 값
    private final float WORLD_WIDTH = 1280;
    private final float WORLD_HEIGHT = 720;
    private Ground ground;
    private GameLevel currentLevel;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int level;
    private Texture background;

    public GameWorld(int level) {
        background = new Texture("sky.png");
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        ground = new Ground(WORLD_WIDTH, 150f, new Texture("ground.png"));
        currentLevel = new GameLevel(level);
        this.level = level;
    }

    public void update(float delta) {
        if (reset()) {
            level = 1;
            switchLevel(1);
        }
        currentLevel.update(delta, ground, WORLD_GRAVITY);
        if (currentLevel.getHighestRocketY() > WORLD_HEIGHT / 2) {
            camera.position.set(WORLD_WIDTH / 2, currentLevel.getHighestRocketY(), 0);
        } else {
            camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        }
        camera.update();
        if (currentLevel.isCleared()) {
            switchLevel(++level);
        } else if (currentLevel.getDistanceBetweenRockets() > WORLD_HEIGHT / 2 + 128f) {
            switchLevel(level);
        }
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        for (float drawY = 0; drawY < 3000; drawY += background.getHeight()) {
            batch.draw(background, 0, drawY, WORLD_WIDTH, background.getHeight());
        }
        ground.render(batch);
        currentLevel.render(batch);
    }

    private void switchLevel(int newLevel) {
        if (currentLevel != null) {
            currentLevel.dispose();
            currentLevel = null;
        }
        currentLevel = new GameLevel(newLevel);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    private boolean reset() {
        return Gdx.input.isKeyJustPressed(Input.Keys.R);
    }

    @Override
    public void dispose() {
        if (ground != null) ground.dispose();
        if (currentLevel != null) currentLevel.dispose();
    }
}
