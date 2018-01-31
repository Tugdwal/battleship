package battleship;

import java.util.Arrays;

import math.Point2D;

/**
 * Ship
 */
public class Ship
{
    private static int MAX_HEALTH = 2;

    private String m_name;
    private Point2D m_position;
    private Point2D m_direction;
    private int m_size;
    private int m_range;
    private boolean[] m_damaged;
    private int m_health;
    private int m_hit_position;

    public Ship(String name, Point2D pos, Point2D dir, int size, int range)
    {
        this.m_name = name;
        this.m_position = pos;
        this.m_direction = dir;
        this.m_size = size;
        this.m_range = range;
        this.m_damaged = new boolean[size];
        this.m_health = MAX_HEALTH;
    }

    public String getName()
    {
        return this.m_name;
    }

    public Point2D getPosition()
    {
        return this.m_position;
    }

    public Point2D getDirection()
    {
        return this.m_direction;
    }

    public int size()
    {
        return this.m_size;
    }

    public void move(Point2D vec)
    {
        this.m_position = this.m_position.add(vec);
    }

    public int range()
    {
        return this.m_range;
    }

    public boolean inRange(Point2D pos)
    {
        for (int i = 0; i < this.m_range; i++) {
            if (this.m_position.add(this.m_direction.scale(-i-1)).equals(pos)) {
                return true;
            }
            if (this.m_position.add(this.m_direction.scale(this.m_size - 1)).add(this.m_direction.scale(i+1)).equals(pos)) {
                return true;
            }
        }

        if (this.m_direction.getX() == 0) {
            if (this.m_direction.getY() > 0) {
                return this.m_position.getY() <= pos.getY()
                    && pos.getY() < this.m_position.getY() + this.m_size
                    && this.m_position.getX() - this.m_range <= pos.getX()
                    && pos.getX() <= this.m_position.getX() + this.m_range;
            } else {
                return this.m_position.getY() - this.m_size < pos.getY()
                    && pos.getY() <= this.m_position.getY()
                    && this.m_position.getX() - this.m_range <= pos.getX()
                    && pos.getX() <= this.m_position.getX() + this.m_range;
            }
        } else {
            if (this.m_direction.getX() > 0) {
                return this.m_position.getX() <= pos.getX()
                    && pos.getX() < this.m_position.getX() + this.m_size
                    && this.m_position.getY() - this.m_range <= pos.getY()
                    && pos.getY() <= this.m_position.getY() + this.m_range;
            } else {
                return this.m_position.getX() - this.m_size < pos.getX()
                    && pos.getX() <= this.m_position.getX()
                    && this.m_position.getY() - this.m_range <= pos.getY()
                    && pos.getY() <= this.m_position.getY() + this.m_range;
            }
        }
    }

    public boolean collide(Point2D target)
    {
        if (destroyed()) {
            return false;
        }

        for (int i = 0; i < this.m_size; i ++) {
            if (target.equals(this.m_position.add(this.m_direction.scale(i)))) {
                this.m_hit_position = i;
                return !this.m_damaged[i];
            }
        }

        return false;
    }

    public int hitPosition()
    {
        return this.m_hit_position;
    }

    public boolean damaged(int n)
    {
        return this.m_damaged[n];
    }

    public void hit(int n)
    {
        this.m_damaged[n] = true;
        this.m_health--;

        if (destroyed()) {
            Arrays.fill(this.m_damaged, true);
        }
    }

    public boolean destroyed()
    {
        return this.m_health == 0;
    }

    @Override
    public String toString()
    {
        return this.m_position + "" + this.m_direction + "(" + this.m_size + ")";
    }
}