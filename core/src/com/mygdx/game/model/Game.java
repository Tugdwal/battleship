package com.mygdx.game.model;

import java.util.ArrayList;

import com.mygdx.game.graphics.BattleInterface;
import com.mygdx.game.model.math.Point2D;
import com.mygdx.game.util.Util;

/**
 * Game
 */
public class Game
{
    private boolean m_running;
    private boolean is_init_loop;
    private Player[] m_players;
    private int m_current;
    private boolean m_move_allowed;
    private boolean m_next_player;
    private com.mygdx.game.BattleShip m_parent;
    private BattleInterface m_battle_interface;

    public Game(com.mygdx.game.BattleShip parent)
    {
        this.m_running = false;
        this.m_players = new Player[2];
        this.m_players[0] = new Player("Player 1");
        this.m_players[1] = new Player("Player 2");
        this.m_current = 0;
        this.m_move_allowed = false;
        this.m_parent = parent;
    }

    public void init()
    {
        this.m_running = false;
        this.is_init_loop = true;
        this.m_battle_interface = m_parent.getBattleInterface();
        this.m_next_player = false;
    }

    public boolean running()
    {
        return this.m_running;
    }

    public void showMoves () {
        Player p = this.m_players[this.m_current];
    	this.m_battle_interface.setQuestion("Choose a ship to move :");
        
        String str = "";
        for (Ship s : p.getShips()) {
            if (!s.destroyed()) {
                str = str + (Util.toCell(s.getPosition()) + " - " + s.getName()) + "\n";
            }
        }
        
        m_battle_interface.setInfo(0, str);
    }
    
    private Ship m_move_ship = null;
    public void move(Point2D pos)
    {
        if (!this.m_move_allowed)
        	return;
        
        Player p = this.m_players[this.m_current];

        if (m_move_ship == null) {
            for (Ship s : p.getShips()) {
                if (s.getPosition().equals(pos) && !s.destroyed()) {
                	m_move_ship = s;
                }
            }

            if (m_move_ship == null) {
                String str = m_battle_interface.getInfoText() + "\nNo ship here";
                m_battle_interface.setInfo(0, str);
                return;
            }
            
            m_battle_interface.setQuestion("Where do you want to place it");
            m_battle_interface.setInfo(0, "");
        }
        else {    
            if (!inRange(pos, m_move_ship.getDirection(), m_move_ship.size())) {
                return;
            } else if (collide(pos, m_move_ship.getDirection(), m_move_ship.size(), p.getShips(), m_move_ship)) {
                return;
            }

            m_move_ship.move(pos);

            m_battle_interface.setInfo(0, m_move_ship.getName() + " moved.");

            System.out.println();
            //System.out.println(showSelf(p));
            this.m_move_ship = null;
            this.m_move_allowed = false;

            m_battle_interface.setQuestion("Where would you like to shoot ?");
        }
        
    }

    public void play(Point2D target)
    {
        Player p = this.m_players[this.m_current];
        Player enemy = this.m_players[enemy()];

        if (!p.inRange(target)) {
        	m_battle_interface.setInfo(1, "Target unreachable");
        	return;
        }

        for (Ship s : enemy.getShips()) {
            if (!s.destroyed()) {
                if (s.collide(target)) {
                    s.hit(s.hitPosition());
                    p.hit(target);

                    m_battle_interface.setInfo(2, "Hit !");

                    if (s.destroyed()) {
                        p.destroy(s);

                        m_battle_interface.setInfo(3, "Enemy " + s.getName() + " destroyed !");

                        if (!enemy.alive()) {
                            m_battle_interface.setTitle("");
                            m_battle_interface.setQuestion("");
                            m_battle_interface.setInfo(2, p.getName() + " wins !");
                            this.m_running = false;
                            return;
                        }
                    }

                	m_next_player = true;
                    return;
                }
            }
        }

        p.miss(target);
        m_battle_interface.setInfo(1, "Miss...");

        this.m_move_allowed = true;
    	m_next_player = true;
    }

    public void next()
    {
        this.m_current = enemy();
		m_battle_interface.setTitle (m_players[m_current].getName());
		if (m_move_allowed)
    		m_battle_interface.moveDialogBox();
    }

    private int enemy()
    {
        return (this.m_current + 1) % 2;
    }

    private boolean inRange(Point2D pos, Point2D dir, int size)
    {
        return pos.inRange(0, 0, 9, 9) && pos.add(dir.scale(size)).inRange(0, 0, 9, 9);
    }

    private boolean collide(Point2D pos, Point2D dir, int size, ArrayList<Ship> ships)
    {
        return collide(pos, dir, size, ships, null);
    }

    private boolean collide(Point2D pos, Point2D dir, int size, ArrayList<Ship> ships, Ship ship)
    {
        for (Ship s : ships) {
            if (ship != null && ship.getPosition().equals(s.getPosition())) {
                return false;
            } else {
                for (int i = 0; i < size; i++) {
                    Point2D p = pos.add(dir.scale(i));

                    for (int j = 0; j < s.size(); j++) {
                        if (p.equals(s.getPosition().add(s.getDirection().scale(j)))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
    
    public void onGroundTouched (int x, int y) {
    	if (is_init_loop) {
    		if (m_players[m_current].getShips().size() < 5)
    			m_battle_interface.dirDialogBox (x, y);
        	else {
        		if (m_current == 1)
        			is_init_loop = false;
        		
        		m_battle_interface.nextDialogBox();
        	}
    	}
    	else if (m_next_player) {
    		m_next_player = false;
    		m_battle_interface.nextDialogBox();
    	}
    	else if (m_move_allowed)
    		move(new Point2D(x/100, y/100));
    	else
    		play(new Point2D (x/100, y/100));
    }
    
    public void init_ships (int x, int y, char dirChar) {
        m_battle_interface.setInfo(1, "");
    	Player p = m_players[m_current];
    	int index = p.getShips().size();
    	
    	if (index < 5) {
    		Point2D pos = new Point2D(x/100, y/100);
    		Point2D dir = Util.getDir(dirChar);
    		ShipFactory.SHIP ship = ShipFactory.SHIP.values()[index];
    		
    		if (inRange(pos, dir, ShipFactory.size(ship)) && 
    				!collide(pos, dir, ShipFactory.size(ship), p.getShips())) 
    			p.add(ShipFactory.buildShip(ShipFactory.SHIP.values()[index], pos, dir));
    		
    		else
                m_battle_interface.setInfo(1, "You can't place this ship here");
    	}
    }
    
    public Player getCurrentPlayer () {return m_players[m_current];}
    public Player[] getPlayers () {return m_players;}
    public void setMoveAllow (boolean move_allow) {m_move_allowed = move_allow;}
}
