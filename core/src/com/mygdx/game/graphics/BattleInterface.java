package com.mygdx.game.graphics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BattleShip;
import com.mygdx.game.model.Player;

public class BattleInterface {
	SpriteBatch mBatch;
	BitmapFont mFont;
	Stage mStage;
	Viewport mViewport;

	String mTitle;
	String mQuestionText;
	String mInfoText;

	private Label mTitleLabel, mQuestionLabel, mInfoLabel, mScoreTitleLabel, mScorePlayer1Label, mScorePlayer2Label;	
	
	BattleShip m_parent;

	public BattleInterface (BattleShip parent, SpriteBatch batch) {
		m_parent = parent;
		mBatch = batch;
		
		mFont = new BitmapFont();
		mFont.setColor(Color.BLACK);
		
		mTitle = "Player 1";
		mQuestionText = "Where would you like to place your ship ?";
		mInfoText = "";

		
		//setup the HUD viewport using a new camera seperate from gamecam
	    //define stage using that viewport and games spritebatch
	    mViewport = new FitViewport(1500, 1000, new OrthographicCamera());
	    mStage = new Stage(mViewport, batch);

	    //define labels using the String, and a Label style consisting of a font and color
	    mTitleLabel = new Label(mTitle, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
	    mQuestionLabel = new Label(mQuestionText, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
	    mInfoLabel = new Label(mInfoText, new Label.LabelStyle(new BitmapFont(), Color.RED));
	    
	    //define a table config
	    Table table = new Table();
	    table.top();
	    table.center();
	    table.setPosition(1000, 0);
	    table.setSize(500, 1000);
	    table.setFillParent(false);

	    //add labels to table, padding the top, and giving them all equal width with expandX
	    table.add(mTitleLabel);
	    table.row();
	    table.add(mQuestionLabel);
	    table.row();
	    table.add(mInfoLabel).expandX();
	    table.row();

	    //Add to stage
	    mStage.addActor(table);


	    //define labels using the String, and a Label style consisting of a font and color
	    mScoreTitleLabel = new Label("Left Ships", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
	    mScorePlayer1Label = new Label("Player 1 : 0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
	    mScorePlayer2Label = new Label("Player 2 : 0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
	    
	    //define a table config
	    Table tableScore = new Table();
	    tableScore.top();
	    tableScore.center();
	    tableScore.setPosition(1000, 500);
	    tableScore.setSize(500, 500);
	    tableScore.setFillParent(false);

	    //add labels to table, padding the top, and giving them all equal width with expandX
	    tableScore.add(mScoreTitleLabel);
	    tableScore.row();
	    tableScore.add(mScorePlayer1Label);
	    tableScore.row();
	    tableScore.add(mScorePlayer2Label);
	    tableScore.row();

	    //Add to stage
	    mStage.addActor(tableScore);
	}
	
	public void draw () {
		Player[] players = m_parent.getGame().getPlayers();
		mScorePlayer1Label.setText("Player 1 : " + (players[0].getShips().size() - players[0].getDestroyed().size()));
		mScorePlayer2Label.setText("Player 2 : " + (players[1].getShips().size() - players[1].getDestroyed().size()));
	    mStage.draw();        
	}
	
	public void dispose () {
		mStage.dispose();
	}
	
	public void setTitle (String str) {
		mTitleLabel.setText(str);
	}
	
	public String getInfoText() {
		return mInfoLabel.getText().toString();
	}
	
	public void setInfo (int type, String str) {
		switch(type) {
		case 0: mInfoLabel.setColor(Color.BLACK);
			break;
			
		case 1: mInfoLabel.setColor(Color.RED);
			break;
			
		case 2: mInfoLabel.setColor(Color.GREEN);
			break;
			
		case 3: mInfoLabel.setColor(Color.BLUE);
			break;
		
		default: 
			mInfoLabel.setColor(Color.BLACK);
			break;
		}
		
		mInfoLabel.setText(str);
	}
	
	public void setQuestion (String str) {
		mQuestionLabel.setText(str);
	}
	
	Dialog dialog;
	public void moveDialogBox () {
		if (dialog != null)
			removeDialog();
		
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		dialog = new Dialog("Choose", skin, "dialog") {
		    public void result(Object obj) {
		        Gdx.input.setInputProcessor(m_parent);
		        removeDialog();

		        if (!((Boolean) obj).booleanValue())
		        	m_parent.getGame().setMoveAllow(false);
		        else
		        	m_parent.getGame().showMoves ();
		        
		    }
		};
		dialog.text("Would you like to move a ship ?");
		dialog.button("Yes", true); //sends "true" as the result
		dialog.button("No", false);  //sends "false" as the result
		dialog.key(Keys.ENTER, true); //sends "true" when the ENTER key is pressed
		dialog.show(mStage);
	    Gdx.input.setInputProcessor(mStage);
	}
	
	public void dirDialogBox (final int x, final int y) {
		if (dialog != null)
			removeDialog();
		
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		dialog = new Dialog("Choose", skin, "dialog") {
		    public void result(Object obj) {
		        Gdx.input.setInputProcessor(m_parent);

	        	m_parent.getGame().init_ships(x, y, ((Character) obj).charValue());
		        removeDialog();
		        
		    }
		};
		dialog.text("Select a direction for your ship ?");
		dialog.button("North", 'N'); //sends "true" as the result
		dialog.button("East", 'E');  //sends "false" as the result
		dialog.key(Keys.N, 'N');
		dialog.key(Keys.E, 'E');
		dialog.show(mStage);
	    Gdx.input.setInputProcessor(mStage);
	}
	
	public void nextDialogBox () {
		if (dialog != null)
			removeDialog();
		
		m_parent.hide ();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		dialog = new Dialog("Choose", skin, "dialog") {
		    public void result(Object obj) {
		        if (((Boolean) obj).booleanValue()) {
			        Gdx.input.setInputProcessor(m_parent);
			        removeDialog();
		        	m_parent.getGame().next();
					m_parent.show ();	
		        }			                
		    }
		};
		dialog.text("Next Player is ready ?");
		dialog.button("OK", true); //sends "true" as the result
		dialog.key(Keys.ENTER, true); //sends "true" when the ENTER key is pressed
		dialog.show(mStage);
	    Gdx.input.setInputProcessor(mStage);
	}
	
	private void removeDialog() {
		dialog.remove();
	}
}
