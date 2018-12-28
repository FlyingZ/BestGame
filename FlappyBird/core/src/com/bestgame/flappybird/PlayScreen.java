package com.bestgame.flappybird;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author mehai
 */
public class PlayScreen implements Screen{

    public final static int V_WIDTH = 240;
    public final static int V_HEIGHT = 400;

    private final FlappyBird game;
    private OrthographicCamera camera;
    
    private Texture bg; 
    private Bird bird;
    private Tube tube;
    
    public PlayScreen(FlappyBird game){
        this.game = game;
        //set camera to area desired to see
        camera = new OrthographicCamera(480, 800);
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        //set background texture
        bg = new Texture("bg.png");
        //create Bird
        bird = new Bird(10, 300);
        tube = new Tube(100);
    }
    
    @Override
    public void show() {
    }

    public void handleInput(){
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }
    
    public void update() {
        handleInput();
        //update bird
        bird.update(Gdx.graphics.getDeltaTime());
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(bg, camera.position.x - camera.viewportWidth / 2, 0);
        game.batch.draw(bird.getTexture(), bird.getX(), bird.getY());
        game.batch.draw(tube.getBottomTube(), tube.getBottomPosition().x, tube.getBottomPosition().y);
        game.batch.draw(tube.getTopTube(), tube.getTopPosition().x, tube.getTopPosition().y);
        game.batch.end();
        
        update();
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
        bg.dispose();
        bird.dispose();
    }

}
