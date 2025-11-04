package io.jbnu.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    public enum GameState {
        RUNNING,
        PAUSED,
        CLEARED,
        FAILED
    }

    private SpriteBatch batch;
    private Sound pauseSound;
    private GameState currentState;
    private GameWorld world;
    private Texture pauseTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        pauseSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        pauseTexture = new Texture("pause.png");
        currentState = GameState.RUNNING;
        world = new GameWorld(1);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);
        input();
        if (currentState == GameState.RUNNING) {
            world.update(Gdx.graphics.getDeltaTime());
        }
        batch.begin();
        world.render(batch);
        if (currentState == GameState.PAUSED) {
            batch.draw(pauseTexture, world.getCamera().position.x - pauseTexture.getWidth() / 2, world.getCamera().position.y - pauseTexture.getHeight() / 2);
        }
        batch.end();
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            pauseSound.play();
            if (currentState == GameState.RUNNING) {
                currentState = GameState.PAUSED;
            } else if (currentState == GameState.PAUSED) {
                currentState = GameState.RUNNING;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
    }

    @Override
    public void dispose() {
        if (world != null) {
            world.dispose();
        }
        if (pauseSound != null) pauseSound.dispose();
        if (pauseTexture != null) pauseTexture.dispose();
        if (batch != null) batch.dispose();
    }
}
