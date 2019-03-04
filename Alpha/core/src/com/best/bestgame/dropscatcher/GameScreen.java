package com.best.bestgame.dropscatcher;

import java.util.Iterator;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.best.bestgame.BestGame;
import com.best.bestgame.Timer;
import com.best.bestgame.menus.EndGameScreen;
import com.best.bestgame.menus.InterGameScreen;


/**
 *The screen of the game
 * @author mehai
 */
public class GameScreen implements Screen{
    
    private static final int NUT_SIZE = 32;
    
    final BestGame game;
    
    Texture dropImage;
    Texture bucketImage;
    Texture bg;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    
    boolean gameLost;
    
    Timer timer;
    
    public GameScreen(final BestGame game){
        this.game = game;
        timer = new Timer(10);
        
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("dropscatcher/nut.png"));
        bucketImage = new Texture(Gdx.files.internal("dropscatcher/catcher.png"));
        bg = new Texture(Gdx.files.internal("dropscatcher/bg_iul.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 480 / 2 - bucket.width / 2; // center the bucket horizontally
        bucket.y = 70; // bottom left corner of the bucket is 20 pixels above
                                        // the bottom screen edge
        bucket.width = bucketImage.getWidth() /4;
        bucket.height = bucketImage.getHeight() /4;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }
    
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 480 - NUT_SIZE);
        raindrop.y = 800;
        raindrop.width = NUT_SIZE;
        raindrop.height = NUT_SIZE;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
    
    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
        /*CHANGE SCREEN HERE - TIMER UP */
        if(!timer.update(delta)){
            game.score += dropsGathered;
            game.lastScreen = this;
            game.setScreen(new InterGameScreen(game, dropsGathered));
            this.dispose();
            //return;
        }

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.font.draw(game.batch, "Score: " + dropsGathered, 20, 800 - 20);
        game.font.draw(game.batch, "" +timer.getSeconds(), camera.viewportWidth - 32, camera.viewportHeight - 20);
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
                game.batch.draw(dropImage, raindrop.x, raindrop.y, NUT_SIZE, NUT_SIZE);
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                bucket.x = touchPos.x - bucket.width / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
                bucket.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
                bucket.x += 300 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
                bucket.x = 0;
        if (bucket.x > 480 - bucket.width)
                bucket.x = 480 - bucket.width;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 250000000)
                spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the 
        // value our drops counter and add a sound effect.
        for(int i = 0; i < raindrops.size; i++){
            Rectangle raindrop = raindrops.get(i);
            raindrop.y -= 300 * delta;
            if (raindrop.y < 70){
                    raindrops.removeIndex(i);
                    i--;
                    /*CHANGE SCREEN HERE - LOST GAME*/
                    gameLost = true;
            }
            else if (raindrop.overlaps(bucket)) {
                    dropsGathered += (int)(Math.random() * 3+1);
                    raindrops.removeIndex(i);
                    i--;
            }
        }
        /* check if game is lost*/
        if(gameLost){
            game.lifePoints--;
            game.score += dropsGathered;
            game.lastScreen = this;
            this.dispose();
            if(game.lifePoints != 0){
                game.setScreen(new InterGameScreen(game, dropsGathered));
            }else{
                game.setScreen(new EndGameScreen(game));
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        bg.dispose();
    }
    
}