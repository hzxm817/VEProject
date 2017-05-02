package com.badlogic.ve_proj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.InputAdapter;

import com.badlogic.ve_proj.Drop;
import com.badlogic.ve_proj.ScrollingBackground;

public class MainMenuScreen implements Screen{
	final Drop game;
	OrthographicCamera camera;
        
        private static final int EXIT_BUTTON_WIDTH = 160;
	private static final int EXIT_BUTTON_HEIGHT = 160;
	private static final int PLAY_BUTTON_WIDTH = 160;
	private static final int PLAY_BUTTON_HEIGHT = 160;
	private static final int EXIT_BUTTON_Y = 100;
	private static final int PLAY_BUTTON_Y = 275;
	private static final int LOGO_WIDTH = 240;
	private static final int LOGO_HEIGHT = 200;
	private static final int LOGO_Y = 450;
        
        Texture playButtonActive;
	Texture playButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	Texture logo;
        
	public MainMenuScreen(final Drop game) {
		this.game = game;
                playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
		exitButtonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");
		logo = new Texture("logo.png");
		
		game.scrollingBackground.setSpeedFixed(true);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
		
		final MainMenuScreen mainMenuScreen = this;
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				//Exit button
				int x = Drop.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && Drop.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && Drop.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
					mainMenuScreen.dispose();
					Gdx.app.exit();
				}
				
				//Play game button
				x = Drop.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && Drop.HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Drop.HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y) {
					mainMenuScreen.dispose();
					game.setScreen(new GameScreen(game));
				}
				
				return super.touchUp(screenX, screenY, pointer, button);
			}
			
		});
               
                        
                        
                        /*camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);*/

	}


        @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
		
		int x = Drop.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
		if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && Drop.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && Drop.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
			game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		} else {
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		}
		
		x = Drop.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
		if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && Drop.HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Drop.HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y) {
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		} else {
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		game.batch.draw(logo, Drop.WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
		
		game.batch.end();
		
                
                
                /*camera.update();
		game.batch.setProjectionMatrix(camera.combined);
                
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}*/
	}
 	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
            Gdx.input.setInputProcessor(null);
	}       
        
}