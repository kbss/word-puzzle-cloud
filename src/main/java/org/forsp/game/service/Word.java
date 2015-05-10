package org.forsp.game.service;

import java.util.Collection;

/**
 * @author Serhii Kryvtsov
 * @since 05/05/2015
 */
public class Word {

    private Long gameId;
    private Collection<Point> points;
    private String word;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Collection<Point> getPoints() {
        return points;
    }

    public void setPoints(Collection<Point> points) {
        this.points = points;
    }
}
