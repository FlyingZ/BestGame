package com.best.bestgame;

import com.badlogic.gdx.Screen;
import com.best.bestgame.dropscatcher.GameScreen;
import java.util.Random;
import com.best.bestgame.menus.InterGameScreen;
import com.best.bestgame.pacman.Board;

/**
 *  Singleton-Factory class designed to return a new Random Game Screen
 * @author mehai
 */
public class Factory {
    private static Factory factory = null;
    private static final int N_GAMES = 4;
    private final Random random;
    private final BestGame game;
    
    private Factory(final BestGame game){ 
        random = new Random();
        this.game = game;
    }
    
    public static Factory getInstance(BestGame game){
        if(factory == null){
            factory = new Factory(game);
        }
        return factory;
    }
    
    public Screen factory(){
        int index = random.nextInt(N_GAMES);
        //FLAPPY BIRD  =  0
        if(index == 0){
            if(game.lastScreen instanceof com.best.bestgame.flappybird.PlayScreen){
                index++;
            }else{
                return new com.best.bestgame.flappybird.PlayScreen(game);
            }
        }
        //PACMAN  =  1
        if(index == 1){
            if(game.lastScreen instanceof Board){
                index++;
            }else{
                return new Board(game);
            }
        }
        //DROPS-CATCHER  =  2
        if(index == 2){
            if(game.lastScreen instanceof GameScreen){
                index++;
            }else{
                return new GameScreen(game);
            }
        }
        //MAZE  =  3
        if(index == 3){
            if(game.lastScreen instanceof com.best.bestgame.maze.PlayScreen){
                return new com.best.bestgame.flappybird.PlayScreen(game);
            }else{
                return new com.best.bestgame.maze.PlayScreen(game);
            }
        }
        if(index >= N_GAMES){
            return new InterGameScreen(game);
        }
        return null;
    }
    
}
