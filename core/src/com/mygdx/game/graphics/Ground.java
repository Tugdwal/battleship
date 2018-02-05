package com.mygdx.game.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.model.Game;

public class Ground {
	int mX;
	int mY;
	Texture mTexture;
	Game mGame;
	
	public Ground (int x, int y, Game game) {
		mX = x;
		mY = y;
		mTexture = new Texture ("WaterSprite.jpg");
		mGame = game;
	}
	
	public Texture draw () {
		return mTexture;
	}
	
	public void dispose () {
		mTexture.dispose();
	}
	
	public void onTouch () {
		mGame.onGroundTouched(mX, mY);
	}
	
	public int getX () {return mX;}
	public int getY () {return mY;}
}
