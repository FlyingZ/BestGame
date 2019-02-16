package com.best.bestgame.maze;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.best.bestgame.BestGame;
import com.best.bestgame.Timer;
import com.best.bestgame.menus.EndGameScreen;
import com.best.bestgame.menus.InterGameScreen;

/**
 *The actual Screen of the game where everything happens.
 * @author mehai
 */
public class PlayScreen implements Screen{

    private static final int BOUNDS_COUNT = 5;

    private BestGame game;

    private Texture bg;
    private Texture maze;
    private Array<Rectangle> mazeBounds;
    private Rectangle finishLine;
    private Ghost ghost;

    private OrthographicCamera cam;

    private Timer timer;
    
    /**
     * Setup of the game components.
     * @param game
     */
    public PlayScreen(final BestGame game){
        this.game = game;
        timer = new Timer(8);
        //define camera and viewport
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 480, 480);
        //initialize textures
        bg = new Texture("maze/mazebg.png");
        maze = new Texture("maze/maze.png");
        //initialize ghost
        ghost = new Ghost(cam);
        //initialize maze bounds and finish line
        //numbers were obtained when i created the maze
        mazeBounds = new Array<Rectangle>();
        mazeBounds.add(new Rectangle(70, 90, 340, 40));
        mazeBounds.add(new Rectangle(370, 130, 40, 130));
        mazeBounds.add(new Rectangle(100, 260, 310, 40));
        mazeBounds.add(new Rectangle(100, 300, 40, 110));
        mazeBounds.add(new Rectangle(140, 368, 134, 42));
        finishLine = new Rectangle(274, 366, 14, 46);
    }

    @Override
    public void show() {
    }

    /**
     * Actions performed after each frame: ghost movement and
     * collision detection.
     * @param delta
     */
    private void update(float delta){
        ghost.update(delta);
        //check collision with finish line
        /*GAME SCREEN CHANGES HERE*/
        if(ghost.getBounds().overlaps(finishLine)){
            game.lastScreen = this;
            game.score += 100;
            this.dispose();
            game.setScreen(new InterGameScreen(game));
            return;
        }
        //check collision with maze bounds
        boolean isInside = false;
        Rectangle ghostBounds = ghost.getBounds();
        for(int i = 0; i < BOUNDS_COUNT - 1; i++){
            if(mazeBounds.get(i).contains(ghostBounds)){
                isInside = true;
                break;
            }else if(ghostBounds.overlaps(mazeBounds.get(i)) &&
                    ghostBounds.overlaps(mazeBounds.get(i+1))){
                isInside = true;
                break;
            }
        }
        //check for the last one too
        if(mazeBounds.get(BOUNDS_COUNT-1).contains(ghostBounds)){
            isInside = true;
        }
        //game lost
        /*GAME SCREEN CHANGES HERE*/
        if(!isInside){
            game.lastScreen = this;
            game.score += 5;
            game.lifePoints--;
            this.dispose();
            if(game.lifePoints != 0){
                game.setScreen(new InterGameScreen(game));
            }else{
                game.setScreen(new EndGameScreen(game));            
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(!timer.update(delta)){
            game.lastScreen = this;
            game.score += 5;
            game.lifePoints--;
            this.dispose();
            if(game.lifePoints != 0){
                game.setScreen(new InterGameScreen(game));
            }else{
                game.setScreen(new EndGameScreen(game));
            }
        }
        
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(maze, 0, 0);
        game.batch.draw(ghost.getTexture(), ghost.getX(), ghost.getY());
        game.font.draw(game.batch, "Score: 5", 20, cam.viewportHeight - 20);
        game.font.draw(game.batch, "" + timer.getSeconds(), cam.position.x + cam.viewportWidth/2 - 50, cam.viewportHeight-20);
        game.batch.end();

        update(delta);
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
        maze.dispose();
        ghost.dispose();
    }

}

