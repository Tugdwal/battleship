package battleship;

import java.util.ArrayList;

import math.Point2D;
import util.Input;
import util.Util;

/**
 * Game
 */
public class Game
{
    private final static String MAP;

    private final static String MESSAGE_POSITION = "Position ?";
    private final static String MESSAGE_DIRECTION = "Direction ?";
    private final static String MESSAGE_TARGET = "Target position ?";

    private final static String ERROR_MAP_LIMIT = "Ship outside limits !";
    private final static String ERROR_COLLISION = "Ship collision !";
    
    static
    {
        StringBuilder s = new StringBuilder();

        s.append("   A B C D E F G H I J   \n");
        s.append("  ---------------------  \n");

        for (int i = 0; i < 10; i++) {
            s.append(i);
            s.append(" | | | | | | | | | | | ");
            s.append(i);
            s.append('\n');
        }
        
        s.append("  ---------------------  \n");
        s.append("   A B C D E F G H I J   ");

        MAP = s.toString();
    }

    private boolean m_running;
    private Player[] m_players;
    private int m_current;
    private boolean m_move_allowed;
    private Input m_input;

    public Game()
    {
        this.m_running = false;
        this.m_players = new Player[2];
        this.m_players[0] = new Player("Player 1");
        this.m_players[1] = new Player("Player 2");
        this.m_current = 0;
        this.m_move_allowed = false;
        this.m_input = new Input();
    }

    public void init()
    {
        this.m_running = true;

        init(0);
        init(1);
    }

    public boolean running()
    {
        return this.m_running;
    }

    public void move()
    {
        if (this.m_move_allowed) {
            Player p = this.m_players[this.m_current];

            System.out.println();
            if (this.m_input.validate(p.getName() + " - Do you want to move a ship ? (Y/N)")) {
                System.out.println();
                System.out.println(showSelf(p));

                System.out.println();
                System.out.println("Choose a ship :");
    
                for (Ship s : p.getShips()) {
                    if (!s.destroyed()) {
                        System.out.println(Util.toCell(s.getPosition()) + " - " + s.getName());
                    }
                }
    
                Ship ship = null;
                Point2D pos;
                do {
                    System.out.println();
                    if (this.m_input.cancel(MESSAGE_POSITION)) {
                        this.m_move_allowed = false;
                        return;
                    }
    
                    pos = Util.getCell(this.m_input.getCell());
    
                    for (Ship s : p.getShips()) {
                        if (s.getPosition().equals(pos) && !s.destroyed()) {
                            ship = s;
                        }
                    }
    
                    if (ship == null) {
                        System.out.println();
                        System.out.println("No ship here");
                    } else {
                        break;
                    }
                } while (true);
    
                Point2D dir;
                int scale;
                do {
                    System.out.println();
                    if (this.m_input.cancel(MESSAGE_DIRECTION)) {
                        this.m_move_allowed = false;
                        return;
                    }
    
                    dir = Util.getDir(this.m_input.getDir());
                    System.out.println();
                    scale = Util.getScale(this.m_input.getScale("How many cells ?"));
    
                    Point2D next_pos = pos.add(dir.scale(scale));
    
                    if (!inRange(next_pos, ship.getDirection(), ship.size())) {
                        System.out.println();
                        System.out.println(ERROR_MAP_LIMIT);
                    } else if (collide(next_pos, ship.getDirection(), ship.size(), p.getShips(), ship)) {
                        System.out.println();
                        System.out.println(ERROR_COLLISION);
                    } else {
                        break;
                    }
                } while (true);
    
                ship.move(dir.scale(scale));

                System.out.println();
                System.out.println(ship.getName() + " moved.");

                System.out.println();
                System.out.println(showSelf(p));
            }
    
            this.m_move_allowed = false;
        }
    }

    public void show()
    {
        Player p = this.m_players[this.m_current];

        String self = showSelf(p);
        String enemy = showEnemy(p);

        StringBuilder m = new StringBuilder();

        for (int i = 0; i < 14; i ++) {
            m.append('\n');
            m.append(self, i * 26, i * 26 + 25);
            m.append("   ");
            m.append(enemy, i * 26, i * 26 + 25);
        }

        System.out.println();
        System.out.println("   " + p.getName() + "                    Enemy");
        System.out.println(m);
    }

    public void end()
    {
        this.m_input.close();
    }

    public void showPlayers()
    {
        System.out.println();
        System.out.println(showSelf(this.m_players[0]));

        System.out.println();
        System.out.println(showSelf(this.m_players[1]));
    }

    public void play()
    {
        Player p = this.m_players[this.m_current];
        Player enemy = this.m_players[enemy()];

        Point2D target;

        do {
            System.out.println();
            target = Util.getCell(this.m_input.getCell(MESSAGE_TARGET));

            if (!p.inRange(target)) {
                System.out.println();
                System.out.println("Target unreachable !");
            } else {
                break;
            }
        } while (true);

        for (Ship s : enemy.getShips()) {
            if (!s.destroyed()) {
                if (s.collide(target)) {
                    s.hit(s.hitPosition());
                    p.hit(target);

                    System.out.println();
                    System.out.println("Hit !");

                    if (s.destroyed()) {
                        p.destroy(s);

                        System.out.println();
                        System.out.println("Enemy " + s.getName() + " destroyed !");

                        if (!enemy.alive()) {
                            System.out.println();
                            System.out.println(p.getName() + " wins !");
                            this.m_running = false;
                        }
                    }

                    return;
                }
            }
        }

        p.miss(target);
        System.out.println();
        System.out.println("Miss...");

        this.m_move_allowed = true;
    }

    public void next()
    {
        this.m_current = enemy();
    }

    private int enemy()
    {
        return (this.m_current + 1) % 2;
    }

    private int index(Point2D pos)
    {
        return (pos.getY() + 2) * 26 + (pos.getX() * 2) + 3;
    }

    private void init(int i)
    {
        Player p = this.m_players[i];

        System.out.println(p.getName());

        System.out.println();
        System.out.println(showSelf(p));
        System.out.println();

        System.out.println("Place your ships");
        System.out.println();
        System.err.println("Position : [A-J][0-9]");
        System.out.println("Direction : N/S/W/E");
        System.out.println();

        for (ShipFactory.SHIP ship : ShipFactory.SHIP.values()) {
            System.out.println();
            System.out.println("==========");
            System.out.println(ship.name());
            System.out.println("Size  : " + ShipFactory.size(ship));
            System.out.println("Range : " + ShipFactory.range(ship));
            System.out.println("==========");

            Point2D pos;
            Point2D dir;
            do {
                System.out.println();
                pos = Util.getCell(this.m_input.getCell(MESSAGE_POSITION));
                System.out.println();
                dir = Util.getDir(this.m_input.getDir(MESSAGE_DIRECTION));

                if (!inRange(pos, dir, ShipFactory.size(ship))) {
                    System.out.println(ERROR_MAP_LIMIT);
                } else if (collide(pos, dir, ShipFactory.size(ship), p.getShips())) {
                    System.out.println(ERROR_COLLISION);
                } else {
                    break;
                }
            } while (true);

            p.add(ShipFactory.buildShip(ship, pos, dir));

            System.out.println();
            System.out.println(ship.name() + " placed !");
            System.out.println();
            System.out.println(showSelf(p));
        }
    }

    private String showSelf(Player p)
    {
        StringBuilder m = new StringBuilder(MAP);
        
        for (Ship s : p.getShips()) {
            if (!s.destroyed()) {
                showRange(m, s.getPosition(), s.getDirection().scale(-1), s.range());
                showRange(m, s.getPosition().add(s.getDirection().scale(s.size() - 1)), s.getDirection(), s.range());

                for (int i = 0; i < s.size(); i ++) {
                    Point2D pos = s.getPosition().add(s.getDirection().scale(i));

                    showRange(m, pos, s.getDirection().rotate(1), s.range());
                    showRange(m, pos, s.getDirection().rotate(-1), s.range());
                }
            }
        }

        for (Ship s : p.getShips()) {
            for (int i = 0; i < s.size(); i++) {
                Point2D pos = s.getPosition().add(s.getDirection().scale(i));

                m.setCharAt(index(pos), s.damaged(i) ? 'x' : 'o');
            }
        }

        return m.toString();
    }

    private String showEnemy(Player p)
    {
        StringBuilder m = new StringBuilder(MAP);

        for (Point2D pos : p.getMissed()) {
            m.setCharAt(index(pos), '*');
        }

        for (Point2D pos : p.getHit()) {
            m.setCharAt(index(pos), 'x');
        }

        for (Ship s : p.getDestroyed()) {
            for (int i = 0; i < s.size(); i++) {
                m.setCharAt(index(s.getPosition().add(s.getDirection().scale(i))), 'X');
            }
        }

        return m.toString();
    }

    private void showRange(StringBuilder m, Point2D pos, Point2D dir, int range)
    {
        for (int i = 0; i < range; i++) {
            Point2D target = pos.add(dir.scale(i+1));
            
            if (target.inRange(0, 0, 9, 9)) {
                m.setCharAt(index(target), '.');
            }
        }
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
}
