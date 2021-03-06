package best.bestem.pacman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PacManGame extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new Board(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		getScreen().dispose();
	}
}