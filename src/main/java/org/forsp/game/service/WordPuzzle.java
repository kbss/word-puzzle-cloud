package org.forsp.game.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015.
 */
public class WordPuzzle {

    private final static Logger LOGGER = Logger.getLogger(WordPuzzle.class.getName());
    private Board board;
    private int dim;
    private Set<String> words;


    public WordPuzzle(String puzzle, Collection<String> puzzleWords) {
        if (puzzle == null) {
            throw new RuntimeException("Invalid char sequence");
        }
        if (puzzleWords == null || puzzleWords.isEmpty()) {
            throw new RuntimeException("Expected at least one word");
        }
        int size = (int) Math.sqrt(puzzle.length());
        if ((size * size) < puzzle.length()) {
            LOGGER.warning("Invalid char sequence, board will be cutted");
        }
        dim = size;

        LOGGER.info("Creating puzzle board with chars: " + puzzle + ", size " + dim + "x" + dim);
        if (dim < 2) {
            throw new RuntimeException("Minimum board size is 2");
        }
        int length = puzzle.length();
        int cells = dim * dim;
        if (length < cells) {
            throw new RuntimeException(String.format("Wrong char sequence, expected: %s chars but found: %s", cells, length));
        }
        if (length > cells) {
            LOGGER.warning(String.format("Char sequence is bigger than board size (expected: %s, actual: %s), extra charters will be ignored", cells, length));
        }
        board = BoardBuilder.build(puzzle, dim);
        LOGGER.info(String.format("Board %sx%s \n%s", dim, dim, board));

        words = new HashSet<>(puzzleWords.size());
        for (String word : puzzleWords) {
            words.add(word.toUpperCase());
        }
    }

    private int getDirection(int x1, int x2) {
        int result = x2 - x1;
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        }
        return -1;
    }

    public Board getBoard() {
        return board;
    }

    public Word getWord(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            throw new RuntimeException("Invalid points");
        }
        char[][] puzzleBoard = board.getBoard();
        StringBuilder sb = new StringBuilder();
        Collection<Point> points = new ArrayList<>();
        int xDirection = getDirection(p1.getX(), p2.getX());
        int yDirection = getDirection(p1.getY(), p2.getY());
        int destX = p2.getX() + xDirection;
        int destY = p2.getY() + yDirection;

        LOGGER.info(p1.getX() + " = " + destX + ", " + p1.getY() + " = " + destY);
        for (int x = p1.getX(), y = p1.getY(); x != destX || y != destY; x = x + xDirection, y = y + yDirection) {
            sb.append(puzzleBoard[x][y]);
            LOGGER.info(puzzleBoard[x][y] + " x = " + x + "->" + p2.getX());
            System.out.println(puzzleBoard[x][y]);
            points.add(new Point(x, y));
        }

        System.out.println("Word: " + sb.toString().toUpperCase());
        if (!words.contains(sb.toString().toUpperCase()) && !words.contains(sb.reverse().toString().toUpperCase())) {
            return null;
        }
        System.out.println(sb.toString());
        Word word = new Word();
        word.setPoints(points);
        word.setWord(sb.toString());
        return word;
    }

    private boolean isValid(int x, int y) {
        return !(x < 0 || y < 0 || x > dim - 1 || y > dim - 1);
    }
}