package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.graphics.BattleInterface;
import com.mygdx.game.graphics.Ground;
import com.mygdx.game.model.Game;

public class BattleShip extends ApplicationAdapter implements InputProcessor {
	SpriteBatch mBatch;
	Texture img;

	ArrayList<Ground> mGrounds;
	BattleInterface mBattleInterface;
	Game mGame;
	boolean mIsInitRoutine;
	private boolean mIsHide;
	
	@Override
	public void create () {
		mGame = new Game (this);
		mIsHide = false;
		Gdx.input.setInputProcessor(this);
		mGrounds = new ArrayList<Ground> ();
		for (int x = 0 ; x < 10 ; x++)
			for (int y = 0 ; y < 10 ; y++) {
				Ground ground = new Ground (x * 100, y * 100, mGame);
				mGrounds.add(ground);
	        }
		
		mBatch = new SpriteBatch();
		mBattleInterface = new BattleInterface(this, mBatch);
		img = new Texture("WaterSprite.jpg");
		mGame.init();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mBatch.begin();

		for (Ground ground : mGrounds) {
			mBatch.draw(ground.draw(), ground.getX(), ground.getY());
		}
		
		if (!mIsHide) {
			mGame.getCurrentPlayer().render(mBatch);
		}

		mBatch.end();
		
		mBattleInterface.draw();
	}
	
	@Override
	public void dispose () {
		mBatch.dispose();
		img.dispose();
		for (Ground ground : mGrounds) {
			ground.dispose();
		}
		mBattleInterface.dispose();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Gdx.app.log("touch ", "touch detected: " + screenX + " & " + (1000 - screenY));
		for (Ground ground : mGrounds) {
			if ((screenX > ground.getX() && screenX <= ground.getX() + 100) && (1000 - screenY > ground.getY() && 1000 - screenY <= ground.getY() + 100)) {
				ground.onTouch();
				break;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public BattleInterface getBattleInterface () {return mBattleInterface;}
	public Game getGame() {return mGame;}
	public void hide () {mIsHide = true;}
	public void show () {mIsHide = false;}
}
