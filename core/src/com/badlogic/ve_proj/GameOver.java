/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badlogic.ve_proj;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

import com.badlogic.ve_proj.Drop;
import com.badlogic.ve_proj.ScrollingBackground;

public class GameOver implements Screen {
	
	private static final int BANNER_WIDTH = 350;
	private static final int BANNER_HEIGHT = 100;
	
	Drop game;
	
	int score, highscore;
	
	Texture gameOverBanner;
	BitmapFont scoreFont;
	
	public GameOver (Drop game, int score) {
		this.game = game;
		this.score = score;
		
		//Get highscore from save file
		Preferences prefs = Gdx.app.getPreferences("spacegame");
		this.highscore = prefs.getInteger("highscore", 0);
		
		//Check if score beats highscore
		if (score > highscore) {
			prefs.putInteger("highscore", score);
			prefs.flush();
		}
		
		//Load textures and fonts
		gameOverBanner = new Texture("game_over.png");
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		
		game.scrollingBackground.setSpeedFixed(true);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	}
	
	@Override
	public void show () {}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
		
		game.batch.draw(gameOverBanner, Drop.WIDTH / 2 - BANNER_WIDTH / 2, Drop.HEIGHT - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);
		
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
		GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.left, false);
		scoreFont.draw(game.batch, scoreLayout, Drop.WIDTH / 2 - scoreLayout.width / 2, Drop.HEIGHT - BANNER_HEIGHT - 15 * 2);
		scoreFont.draw(game.batch, highscoreLayout, Drop.WIDTH / 2 - highscoreLayout.width / 2, Drop.HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);
		
		float touchX = game.cam.getInputInGameWorld().x, touchY = Drop.HEIGHT - game.cam.getInputInGameWorld().y;
		
		GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
		GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");
		
		float tryAgainX = Drop.WIDTH / 2 - tryAgainLayout.width /2;
		float tryAgainY = Drop.HEIGHT / 2 - tryAgainLayout.height / 2;
		float mainMenuX = Drop.WIDTH / 2 - mainMenuLayout.width /2;
		float mainMenuY = Drop.HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;
		
		//Checks if hovering over try again button
		if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY)
			tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0, Align.left, false);
		
		//Checks if hovering over main menu button
		if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY)
			mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);
		
		//If try again and main menu is being pressed
		if (Gdx.input.isTouched()) {
			//Try again
			if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
				this.dispose();
				game.batch.end();
				game.setScreen(new GameScreen(game));
				return;
			}
			
			//main menu
			if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
				this.dispose();
				game.batch.end();
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
		
		//Draw buttons
		scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
		scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}