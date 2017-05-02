package com.badlogic.ve_proj;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

import com.badlogic.ve_proj.MainMenuScreen;
import com.badlogic.ve_proj.GameCamera;
import com.badlogic.ve_proj.ScrollingBackground;

public class Drop extends Game {
	
        public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static boolean IS_MOBILE = false;
	public BitmapFont font;
	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	public GameCamera cam;
        
        @Override
	public void create() {
		batch = new SpriteBatch();
                cam = new GameCamera(WIDTH, HEIGHT);
		
		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS)
			IS_MOBILE = true;
		IS_MOBILE = true;
		
		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		//this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); //important!
	}
	
	public void dispose() {
		batch.dispose();
		//font.dispose();
	}

}