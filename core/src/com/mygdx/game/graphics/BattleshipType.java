package com.mygdx.game.graphics;

public enum BattleshipType {
	LITTLE(1),
	MEDIUM(2);
	
	int mType;
	BattleshipType (int type){this.mType = type;}
}