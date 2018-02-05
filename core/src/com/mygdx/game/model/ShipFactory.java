package com.mygdx.game.model;

import com.mygdx.game.model.math.Point2D;

/**
 * ShipFactory
 */
public class ShipFactory
{
    public static final int SHIP_COUNT = 5;

    public static enum SHIP
    {
        Carrier,
        Cruiser,
        Battleship,
        Submarine,
        Destroyer
    }

    public static Ship buildShip(SHIP ship, Point2D pos, Point2D dir)
    {
        return new Ship(ship.name(), pos, dir, size(ship), range(ship));
    }

    public static int size(SHIP ship)
    {
        switch (ship) {
            case Carrier:
                return 5;
            case Cruiser:
                return 4;
            case Battleship:
                return 3;
            case Submarine:
                return 3;
            case Destroyer:
                return 2;
            default:
                return 0;
        }
    }

    public static int range(SHIP ship)
    {
        switch (ship) {
            case Carrier:
                return 2;
            case Cruiser:
                return 2;
            case Battleship:
                return 2;
            case Submarine:
                return 4;
            case Destroyer:
                return 5;
            default:
                return 0;
        }
    }
}