package org.forsp.game.domain;

import java.util.Arrays;

/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015
 */
public class Board {

    private char[][] board;

    public Board(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] c : board) {
            sb.append(Arrays.toString(c)).append("\n");
        }
        return sb.toString();
    }
}
