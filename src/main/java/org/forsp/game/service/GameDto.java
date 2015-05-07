package org.forsp.game.service;

/**
 * @author Serhii Kryvtsov
 * @since 08/05/2015
 */
public class GameDto {

    private Long id;
    private String name;
    private char[][] board;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
}
