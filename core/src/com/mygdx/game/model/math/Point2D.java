package com.mygdx.game.model.math;

/**
 * Point2D
 */
public class Point2D
{
    private int m_x;
    private int m_y;

    public Point2D()
    {
        this(0, 0);
    }

    public Point2D(int x, int y)
    {
        this.m_x = x;
        this.m_y = y;
    }

    public int getX()
    {
        return this.m_x;
    }

    public int getY()
    {
        return this.m_y;
    }

    public Point2D add(Point2D other)
    {
        return new Point2D(this.m_x + other.m_x, this.m_y + other.m_y);
    }

    public Point2D scale(int s)
    {
        return new Point2D(this.m_x * s, this.m_y * s);
    }

    public Point2D rotate(int dir)
    {
        return new Point2D(-this.m_y * dir, this.m_x * dir);
    }

    public boolean equals(Point2D other)
    {
        return this.m_x == other.m_x && this.m_y == other.m_y;
    }

    public boolean inRange(int x_min, int y_min, int x_max, int y_max)
    {
        return this.m_x >= x_min && this.m_x <= x_max && this.m_y >= y_min && this.m_y <= y_max;
    }

    @Override
    public String toString()
    {
        return "(" + this.m_x + ", " + this.m_y + ")";
    }
}
