package org.forsp.game.service;

import java.util.Collection;

/**
 * @author Serhii Kryvtsov
 * @since 08/05/2015
 */
public class GameDto {

    private Long id;
    private String name;
    private char[][] board;
    private Collection<String> words;

    public Collection<String> getWords() {
        return words;
    }

    public void setWords(Collection<String> words) {
        this.words = words;
    }

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
