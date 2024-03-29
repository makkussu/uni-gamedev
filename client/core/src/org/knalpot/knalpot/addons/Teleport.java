package org.knalpot.knalpot.addons;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Teleport {
    private static final int FRAME_COLS = 10;
    private static final int FRAME_ROWS = 1;
    private static final float FRAME_DURATION = 0.15f;
    private static final float FRAME_DURATION_FAST = 0.1f;

    private World world;

    private Animation<TextureRegion> animation;
    private float stateTime;
    private SpriteBatch batch;

    private float width;
    private float height;
    private float x;
    private float y;

    private Rectangle bounds;

    private boolean isEKeyPressed;
    private boolean isAnimationPlayed;

    public Sound swooshSound;

    public Teleport(float width, float height, float x, float y, World world, SpriteBatch batch) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.world = world;
        this.batch = batch;

        swooshSound = Gdx.audio.newSound(Gdx.files.internal("teleport.mp3"));

        isEKeyPressed = false;
        isAnimationPlayed = false;

        // Load the spritesheet
        Texture texture = new Texture("teleportExtended.png");
        TextureRegion[][] regions = TextureRegion.split(texture, 20, 48);

        bounds = new Rectangle(this.x, this.y, this.width, this.height);

        // Create the animation
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = regions[i][j];
            }
        }
        animation = new Animation<>(FRAME_DURATION, frames);
        stateTime = 0f;
    }

    public void render() {
        TextureRegion currentFrame;
        if (!isEKeyPressed) {
            stateTime += Gdx.graphics.getDeltaTime();
            int frameIndex = ((int) (stateTime / FRAME_DURATION) % 10) + 1;
            if (frameIndex <= 5) {
                currentFrame = animation.getKeyFrames()[frameIndex - 1];
            } else {
                currentFrame = animation.getKeyFrames()[10 - frameIndex];
            }
        } else {
            if (!isAnimationPlayed) {
                stateTime += Gdx.graphics.getDeltaTime();
                currentFrame = animation.getKeyFrame(stateTime, false);
                if (animation.isAnimationFinished(stateTime)) {
                    isAnimationPlayed = true;
                }
            } else {
                currentFrame = animation.getKeyFrames()[9];
            }
            // set the frame duration to the faster value
            animation.setFrameDuration(FRAME_DURATION_FAST);
        }
    
        // Render the current frame
        float frameWidth = currentFrame.getRegionWidth() * (2f * width / currentFrame.getRegionWidth());
        float frameHeight = currentFrame.getRegionHeight() * (2f * height / currentFrame.getRegionHeight());        
        batch.draw(currentFrame, this.x, this.y, frameWidth, frameHeight);
    
        if (Gdx.input.isKeyJustPressed(Keys.E)) {
            if (world.getPlayer().canUseTeleport && !isEKeyPressed) {
                isEKeyPressed = true;
                stateTime = 0f;
                swooshSound.play();
            }
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
