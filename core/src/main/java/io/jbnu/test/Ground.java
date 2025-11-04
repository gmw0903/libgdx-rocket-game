package io.jbnu.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Ground implements Disposable {
    public static final float GROUND_X = 0f;
    public static final float GROUND_Y = 0f;
    public final float width, height;
    private final Rectangle bounds = new Rectangle();
    private final Texture texture;

    public Ground(float worldWidth, float height, Texture texture) {
        this.width = worldWidth;
        this.height = height;
        this.texture = texture;
        this.bounds.set(GROUND_X, GROUND_Y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, GROUND_X, GROUND_Y, width, height);
    }

    @Override
    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
