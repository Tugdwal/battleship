package battleship;

import java.util.ArrayList;

import math.Point2D;

/**
 * Player
 */
public class Player
{
    private String m_name;
    
    private ArrayList<Ship> m_ships;
    private ArrayList<Ship> m_destroyed;

    private ArrayList<Point2D> m_missed;
    private ArrayList<Point2D> m_hit;

    public Player(String name)
    {
        this.m_name = name;

        this.m_ships = new ArrayList<>();
        this.m_destroyed = new ArrayList<>();

        this.m_missed = new ArrayList<>();
        this.m_hit = new ArrayList<>();
    }

    public String getName()
    {
        return this.m_name;
    }

    public ArrayList<Ship> getShips()
    {
        return this.m_ships;
    }

    public ArrayList<Ship> getDestroyed()
    {
        return this.m_destroyed;
    }

    public ArrayList<Point2D> getMissed()
    {
        return this.m_missed;
    }

    public ArrayList<Point2D> getHit()
    {
        return this.m_hit;
    }

    public void add(Ship s)
    {
        this.m_ships.add(s);
    }

    public void miss(Point2D pos)
    {
        this.m_missed.add(pos);
    }

    public void hit(Point2D target)
    {
        this.m_hit.add(target);
    }

    public void destroy(Ship s)
    {
        this.m_destroyed.add(s);
    }

    public boolean alive()
    {
        for (Ship s : this.m_ships) {
            if (!s.destroyed()) {
                return true;
            }
        }

        return false;
    }

    public boolean inRange(Point2D pos)
    {
        for (Ship s : this.m_ships) {
            if (!s.destroyed() && s.inRange(pos)) {
                return true;
            }
        }

        return false;
    }
}
