package org.forsp.game.service;

/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015
 */
public class BoardBuilder {

    public static Board build(String words, int dim) {
        char[][] board = new char[dim][dim];
        char[] chars = words.toCharArray();
        for (int i = 0; i < dim; i++) {
            System.arraycopy(chars, i * dim, board[i], 0, dim);
        }
        return new Board(board);
    }
}
