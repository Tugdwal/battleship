package com.mygdx.game.util;

import java.util.Scanner;

/**
 * Input
 */
public class Input
{
    public final static String PATTERN_YES_OR_NO = "([yY]([eE][sS])?)|([nN][oO]?)";
    public final static String PATTERN_CANCEL = "[cC]([aN][cC][eE][lL])?";
    public final static String PATTERN_DIRECTION = "[nNsSeEwW]";
    public final static String PATTERN_CELL = "[a-jA-J][0-9]";
    public final static String PATTERN_ONE_OR_TWO = "[12]";

    private Scanner m_scanner;

    public Input()
    {
        this.m_scanner = new Scanner(System.in);
    }

    public void close()
    {
        this.m_scanner.close();
    }

    public String get(String pattern, String error)
    {
        while (!this.m_scanner.hasNext(pattern)) {
            System.out.println("Error - " + error);
            this.m_scanner.next();
        }

        return this.m_scanner.next(pattern);
    }

    public char getChar(String pattern, String error)
    {
        return Character.toUpperCase(get(pattern, error).charAt(0));
    }

    public String getCell()
    {
        return get(PATTERN_CELL, "Format -> [A-J][0-9]");
    }

    public String getCell(String message)
    {
        System.out.println(message);
        return getCell();
    }

    public char getDir()
    {
        return getChar(PATTERN_DIRECTION, "Format -> N/S/E/W");
    }

    public char getDir(String message)
    {
        System.out.println(message);
        return getDir();
    }

    public char getScale()
    {
        return get(PATTERN_ONE_OR_TWO, "1 or 2").charAt(0);
    }

    public char getScale(String message)
    {
        System.out.println(message);        
        return getScale();
    }

    public boolean validate()
    {
        return getChar(PATTERN_YES_OR_NO, "Format -> Yes/No") == 'Y';
    }

    public boolean validate(String message)
    {
        System.out.println(message);
        return validate();
    }

    public boolean cancel()
    {
        boolean cancel = this.m_scanner.hasNext(PATTERN_CANCEL);

        if (cancel) {
            this.m_scanner.next();
        }

        return cancel;
    }

    public boolean cancel(String message)
    {
        System.out.println(message);
        return cancel();
    }
}