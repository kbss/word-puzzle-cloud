package org.forsp.game.service;

import org.forsp.game.service.Point;

import java.util.Collection;

/**
 * @author Serhii Kryvtsov
 * @since 05/05/2015
 */
public class Word {

    private Collection<Point> points;
    private String word;

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
