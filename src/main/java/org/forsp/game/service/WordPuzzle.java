package org.forsp.game.service;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015.
 */
public class WordPuzzle {

    private final static Logger LOGGER = Logger.getLogger(WordPuzzle.class.getName());
    private Board board;
    private int dim;
    private Map<Character, Collection<Point>> charMap;
    private Set<String> words;
    private Point[] SEARCH_DIRECTIONS = {
            new Point(0, 1), new Point(0, -1), new Point(1, 0),
            new Point(1, 1), new Point(1, -1), new Point(-1, 0),
            new Point(-1, 1), new Point(-1, -1)
    };

    public WordPuzzle(String puzzle, Set<String> words) {
        if (puzzle == null) {
            throw new RuntimeException("Invalid char sequence");
        }
        if (words == null || words.isEmpty()) {
            LOGGER.warning("Expected at least one word");
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
        fillCharMap();
        LOGGER.info(String.format("Board %sx%s \n%s", dim, dim, board));
        this.words = words;
    }

    public Board getBoard() {
        return board;
    }

    private Collection<Point> searchWord(String word, Point startPos, Point direction) {
        int wordLength = word.length();
        char[] chars = word.toCharArray();
        char[][] puzzleBoard = board.getBoard();
        int x = startPos.getX();
        int y = startPos.getY();
        Collection<Point> points = new ArrayList<Point>(wordLength);
        for (int i = 0; i < wordLength; i++) {
            if (!isValid(x, y)) {
                return null;
            }
            char c = chars[i];
            char found = puzzleBoard[x][y];
            if (c != found) {
                return null;
            }
            points.add(new Point(x, y));
            x = x + direction.getX();
            y = y + direction.getY();

        }
        return points;
    }

    private void fillCharMap() {
        charMap = new HashMap<Character, Collection<Point>>();
        char[][] puzzleBoard = board.getBoard();
        for (int x = 0; x < puzzleBoard.length; x++) {
            for (int y = 0; y < puzzleBoard.length; y++) {
                char c = puzzleBoard[x][y];
                Collection<Point> chars = charMap.get(c);
                if (chars == null) {
                    chars = new ArrayList<Point>();
                    charMap.put(c, chars);
                }
                chars.add(new Point(x, y));
            }
        }
    }

    public Word getWord(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            throw new RuntimeException("Invalid points");
        }
        char[][] puzzleBoard = board.getBoard();
        StringBuilder sb = new StringBuilder();
        for (int x = p1.getX(), y = p1.getY(); x < p2.getX(); x++, y++) {
            sb.append(puzzleBoard[x][y]);

            System.out.println(puzzleBoard[x][y]);
        }
        System.out.println(sb.toString());
        return null;
    }

    public Word searchWord(String word) {
        long start = System.currentTimeMillis();
        LOGGER.info("Searching word: " + word);
        char[] chars = word.toCharArray();

        if (chars.length > dim) {
            LOGGER.info("Word bigger than board size");
            return null;
        }
        Collection<Point> points = charMap.get(chars[0]);
        if (points == null || points.isEmpty()) {
            LOGGER.info(String.format("There is no char: %s on board", chars[0]));
            return null;
        }
        for (Point startPos : points) {
            for (Point searchDirection : SEARCH_DIRECTIONS) {
                Collection<Point> resultPoints = searchWord(word, startPos, searchDirection);
                if (resultPoints != null && !resultPoints.isEmpty()) {
                    LOGGER.info(String.format("Word found, Search time: %s s.", (System.currentTimeMillis() - start) / 1000f));
                    Word result = new Word();
                    result.setWord(word);
                    result.setPoints(resultPoints);
                    return result;
                }
            }
        }
        LOGGER.info(String.format("Word %s not found", word));
        return null;
    }

    private boolean isValid(int x, int y) {
        return !(x < 0 || y < 0 || x > dim - 1 || y > dim - 1);
    }
}