package com.mygdx.game.util;

import com.mygdx.game.model.math.Point2D;

/**
 * Util
 */
public class Util
{
    public static Point2D getCell(String cell)
    {
        return new Point2D(cell.charAt(0) - 'A', Character.getNumericValue(cell.charAt(1)));
    }

    public static String toCell(Point2D pos)
    {
        return (char)(pos.getX() + 'A') + "" + pos.getY();
    }

    public static Point2D getDir(char dir)
    {
        switch (dir) {
            case 'N':
                return new Point2D(0, -1);
            case 'S':
                return new Point2D(0, 1);
            case 'E':
                return new Point2D(1, 0);
            case 'W':
                return new Point2D(-1, 0);
            default:
                return new Point2D();
        }
    }

    public static int getScale(char scale)
    {
        return Character.getNumericValue(scale);
    }
}
