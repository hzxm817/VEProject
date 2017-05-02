package com.badlogic.ve_proj;

import java.util.Iterator;
import com.badlogic.ve_proj.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

public class GameScreen implements Screen {
	final Drop game;
        Timer time;
	Texture dropImage;
	Texture bucketImage;
        Texture coinImage;
	Sound dropSound;
        Sound coinSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
        Array<Rectangle> coins;
	long lastDropTime;
        long lastCoinTime;
	int dropsGathered;
        int coinsGathered;
        
	public GameScreen(final Drop gam) {
		this.game = gam;

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
                coinImage = new Texture(Gdx.files.internal("coin.png"));
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
                coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 480 / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
                coins = new Array<Rectangle>();
		spawnCoin();
                
                game.scrollingBackground.setSpeedFixed(false);
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 480 - 64);
		raindrop.y = 800;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
        
        private void spawnCoin() {
		Rectangle coin = new Rectangle();
		coin.x = MathUtils.random(0, 480-64);
		coin.y = 800;
		coin.width = 64;
		coin.height = 64;
		coins.add(coin);
		lastCoinTime = TimeUtils.nanoTime();
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

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
                game.batch.draw(coinImage, 0,0);
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 800);
                game.font.draw(game.batch, "X: " + coinsGathered, 45, 15);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
                for (Rectangle coin : coins) {
			game.batch.draw(coinImage, coin.x, coin.y);
		}
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 480-64;
		if (bucket.x > 480 - 64)
			bucket.x = 0;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();
                if (TimeUtils.nanoTime() - lastCoinTime > 1000000000)
			spawnCoin();
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the 
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iterd = raindrops.iterator();
		while (iterd.hasNext()) {
			Rectangle raindrop = iterd.next();
			raindrop.y -= 250 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iterd.remove();                  
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
				iterd.remove();
			}
		}
		Iterator<Rectangle> iterc = coins.iterator();
		while (iterc.hasNext()) {
			Rectangle coin = iterc.next();
			coin.y -= 400 * Gdx.graphics.getDeltaTime();
			if (coin.y + 64 < 0)
				iterc.remove();
			if (coin.overlaps(bucket)) {
				coinsGathered++;
				coinSound.play();
				iterc.remove();
			}
		} 
                
                if (coinsGathered >= 30){
                    this.dispose();
                    game.setScreen(new GameOver(game, coinsGathered));
                }
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
                coinSound.dispose();
		rainMusic.dispose();
                coinImage.dispose();
	}

}