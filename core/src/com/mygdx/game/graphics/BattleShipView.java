package com.mygdx.game.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.Ship;

public class BattleShipView {
	int mX;
	int mY;
	Texture mTexture;
	Texture mDamagedTexture;
	boolean mIsVisible;
	Sprite  mSprite;
	Ship mParent;
	
	public BattleShipView (Ship parent, int x, int y, String name) {
		mParent = parent;
		mX = x;
		mY = y;
		mIsVisible = false;

		mTexture = new Texture (name + ".png");
		
		mSprite = new com.badlogic.gdx.graphics.g2d.Sprite(mTexture);
		mSprite.setPosition(mX, mY);
		
		mDamagedTexture = new Texture ("Fire.png");
	}
	
	public void draw (SpriteBatch batch) {
		if (mIsVisible) {
			mSprite.draw(batch, 1);

			boolean[] damages = mParent.getDamages();
			for (int loop = 0 ; loop < damages.length ; loop++) {
				if(damages[loop]) {
					if (mSprite.getRotation() == 0)
						batch.draw(mDamagedTexture, mX, mY + (damages.length-1 - loop) * 100);
					else
						batch.draw(mDamagedTexture, mX + (loop) * 100, mY + (mParent.size () -1) * 100);
				}
			}
		}
	}
	
	public void dispose () {
		mTexture.dispose();
		mDamagedTexture.dispose();
	}

	public void setX (int x) {
		mX = x;
		mSprite.setPosition(mX, mY);
	}
	public void setY (int y) {
		mY = y;
		mSprite.setPosition(mX, mY);
	}
	
	public void setPosition(int x, int y) {
		mX = x;
		mY = y;
		mSprite.setPosition(mX, mY);
	}
	public int getX () {return mX;}
	public int getY () {return mY;}
	public boolean isVisible () {return mIsVisible;}
	public void setVisible () {mIsVisible = true;}
	public void setHide () {mIsVisible = false;}
	public void setHorizontal() {
		if (mSprite.getRotation() != 90)
			mSprite.setOrigin(mSprite.getWidth()/2, mSprite.getHeight() - 50);
			mSprite.setRotation(90);
		}
	public void setVertical() {
		if (mSprite.getRotation() != 0)
			mSprite.setOrigin(mSprite.getWidth()/2, mSprite.getHeight() + 50);
			mSprite.setRotation(0);
		}

}
